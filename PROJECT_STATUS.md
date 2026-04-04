# All Citizens — Project Status & Continuation Guide

> Last updated: 2026-04-01
> Purpose: Provide all necessary context for any developer (or AI assistant) to continue this project without ambiguity or risk of breaking existing work.

---

## 1. Project Overview

**All Citizens** is a modernization of the legacy "E-Atendimento" municipal ombudsman/citizen service platform. The original system ran on Oracle 11g with a monolithic architecture. The new system is being rebuilt from scratch with modern tools.

### Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 17, Spring Boot 3.3.5 |
| Architecture | Hexagonal / Clean Architecture (Maven multi-module) |
| Database | PostgreSQL 16 + PostGIS |
| Migrations | Flyway |
| Authentication | Keycloak 24 (OAuth2 Resource Server) |
| Frontend | Next.js (not yet started) |
| Mobile | TBD (not yet started) |
| Containerization | Docker Compose for local dev |

### Project Location

```
all-citizens/
├── backend/                  # Java backend (this is the active work)
│   ├── pom.xml               # Parent POM (packaging: pom)
│   ├── docker-compose.yml    # PostgreSQL + Keycloak
│   ├── domain/               # Module: pure domain logic (no frameworks)
│   ├── application/          # Module: use cases, commands, application services
│   └── infrastructure/       # Module: Spring Boot, REST, JPA, Flyway, Security
```

---

## 2. Architecture Rules (STRICT — do not violate)

### Module Dependencies

```
domain         → NO external dependencies (pure Java)
application    → depends on domain only (+ jakarta.transaction-api for @Transactional)
infrastructure → depends on application (and transitively domain)
```

### Key Conventions

1. **Domain entities**: Private constructor, `static create(...)` factory + `static reconstitute(...)` for persistence mapping. No framework annotations.
2. **Application services**: Do NOT implement use case interfaces (except ServiceRequest/Tenant which directly implement some — see BeanConfiguration). Use `jakarta.transaction.Transactional`.
3. **Use case interfaces**: Single-method functional interfaces in `application/{feature}/usecase/`.
4. **BeanConfiguration** (`infrastructure/config/BeanConfiguration.java`): Wires application services and exposes use case beans via method references (or a single `@Bean` for the application service where that avoids duplicate Spring beans). This is the central wiring point — **always update it when adding new features**.
5. **Controllers** normally inject use case interfaces. For aggregates where the same `*ApplicationService` instance implements multiple use-case interfaces, some REST controllers inject the **application service directly** (`TenantApplicationService`, `ServiceRequestApplicationService`, `ForwardingReasonApplicationService`, `HistoryTypeApplicationService`, `RequestHistoryApplicationService`) so Spring does not see two beans for the same interface.
6. **REST mappers** (`@Component`): Convert between DTOs ↔ Commands/Results.
7. **Persistence mappers** (`@Component`): Convert between JPA entities ↔ Domain entities using `reconstitute()`.
8. **JPA entities**: Use `@Id` WITHOUT `@GeneratedValue` (domain generates UUIDs). Use `@JdbcTypeCode(SqlTypes.JSON)` for JSONB columns.
9. **PostgreSQL enums**: Handled by `?stringtype=unspecified` in the JDBC URL — store as `String` in JPA entities.
10. **Schema management**: `hibernate.ddl-auto: none` — Flyway is the source of truth.

### Bean Wiring Pattern

For services that **do NOT** implement use case interfaces (Department, Subject, Subdivision, Address, Person, CatalogService):
```java
@Bean
public XxxApplicationService xxxApplicationService(XxxRepository repo) {
    return new XxxApplicationService(repo);
}
@Bean
public CreateXxxUseCase createXxxUseCase(XxxApplicationService svc) {
    return svc::create;  // method reference
}
```

For **Tenant**, **Service Request**, **Forwarding reasons**, **History**, **Request history**: expose a single `@Bean` for `*ApplicationService` and inject that type from REST controllers (avoids duplicate Spring beans for the same runtime instance).

---

## 3. Features Implemented

### 3.1 Tenant (`domain.tenant` / `application.tenant` / `infrastructure...tenant`)
- **CRUD**: Create, Get, Update, List (paginated + optional `q`), Delete
- **API**: `/api/v1/tenants`
- **DB table**: `tenant` (id, name, code, active, config JSONB, created_at, updated_at)
- **Notes**: `TenantController` injects `TenantApplicationService` (implements create/get/update/list; `delete` is a service method).
- **Tests**: Domain (13), Application (9), Controller (5)

### 3.2 Department (`domain.department` / `application.department` / `infrastructure...department`)
- **CRUD**: Create, Get, Update, List by tenant, Delete
- **API**: `/api/v1/departments`
- **DB table**: `department` (id, tenant_id, parent_id, name, abbreviation, email, active, can_respond, is_root, icon_url, display_order, created_at, updated_at)
- **Notes**: Supports hierarchical structure (parent_id). ApplicationService does NOT implement use case interfaces.
- **Tests**: Domain (11), Application (9), Controller (6)

### 3.3 Service Request (`domain.request` / `application.request` / `infrastructure...request`)
- **Operations**: Create, Get, Update, List by tenant (paginated + optional `q` search), Close, Cancel
- **API**: `/api/v1/service-requests`
- **DB table**: `service_request` (30+ columns including tenant_id, protocol, service_id, person_id, address_id, channel, status, priority, dynamic_fields JSONB, etc.)
- **Domain enums**: `Channel`, `RequestStatus`, `Priority`, `EmotionalState` (in `domain.request`)
- **Notes**: `ServiceRequestController` injects `ServiceRequestApplicationService` (single bean). Use cases remain in the application module for ports/testing.
- **Tests**: Domain (12), Application (5), Controller (6)

### 3.4 Service Catalog — Subject (`domain.subject` / `application.subject` / `infrastructure...subject`)
- **CRUD**: Create, Get, Update, List by tenant, Delete
- **API**: `/api/v1/subjects`
- **DB table**: `subject` (id, tenant_id, department_id, name, active, visible_web, visible_app, created_at, updated_at)
- **Tests**: Domain (13), Application (8), Controller (6)

### 3.5 Service Catalog — Subdivision (`domain.subdivision` / `application.subdivision` / `infrastructure...subdivision`)
- **CRUD**: Create, Get, Update, List ALL (no tenant filter), Delete
- **API**: `/api/v1/subdivisions`
- **DB table**: `subdivision` (id, name, active) — simple lookup table, NO tenant_id, NO timestamps
- **Tests**: Domain (8), Application (7), Controller (6)

### 3.6 Service Catalog — CatalogService (`domain.catalog` / `application.catalog` / `infrastructure...catalog`)
- **CRUD**: Create, Get, Update, List by tenant, Delete
- **API**: `/api/v1/catalog-services`
- **DB table**: `service` (id, tenant_id, subject_id, subdivision_id, department_id, display_name, description, sla_days, default_priority, allows_anonymous, allows_multi_forward, visible_web, visible_app, dynamic_fields JSONB, active, created_at, updated_at)
- **Notes**: Domain entity named `CatalogService` to avoid naming conflicts. Uses `Priority` enum from `domain.request`.
- **Tests**: Domain (16), Application (7*), Controller (6)

### 3.7 Address (`domain.address` / `application.address` / `infrastructure...address`)
- **CRUD**: Create, Get, Update, Delete (no list endpoint)
- **API**: `/api/v1/addresses`
- **DB table**: `address` (id, zip_code, street, number, complement, district_id, city_id, state_code char(2), landmark, latitude, longitude, created_at, updated_at)
- **Notes**: Standalone entity (no tenant_id). Referenced by person.address_id and service_request.address_id. `state_code` is `char(2)` in DB — JPA entity uses `columnDefinition = "char(2)"`.
- **Tests**: Domain (7), Application (6), Controller (6)

### 3.8 Person + Individual (`domain.person` / `application.person` / `infrastructure...person`)
- **CRUD**: Create, Get, Update, List by tenant, Delete
- **API**: `/api/v1/persons`
- **DB tables**: `person` + `individual` (1:1 when type=INDIVIDUAL)
- **Domain model**: Single `Person` aggregate root combines data from both tables. Enums: `PersonType` (INDIVIDUAL, ORGANIZATION), `Gender` (MALE, FEMALE, NOT_INFORMED).
- **Persistence**: Two JPA entities (`PersonJpaEntity` + `IndividualJpaEntity`), `PersonRepositoryImpl` manages both, `PersonPersistenceMapper` combines/splits them.
- **Notes**: Currently only INDIVIDUAL type is implemented. Organization can be added later following the same aggregate pattern.
- **Tests**: Domain (11), Application (8), Controller (7)

### 3.9 Audit log (`domain.audit` / `application.audit` / filter + persistence)
- **Behaviour**: `AuditLoggingFilter` persiste pedidos mutativos (`POST`, `PUT`, `PATCH`, `DELETE`) em `/api/v1/**` exceto `/api/v1/public/**`; ator a partir do JWT (`sub`, `preferred_username`, authorities). Opcional: `app.audit.log-get-requests=true` para também gravar GET.
- **API**: `GET /api/v1/audit-logs` — paginação (`page`, `size`), filtros opcionais `from` / `to` (ISO-8601); **apenas Supervisão** (`SecurityConfig`).
- **DB table**: `audit_log` (`V3__audit_log.sql`); JPA `AuditLogJpaEntity`, port `AuditLogRepository` + `AuditLogRepositoryImpl`.
- **Tests**: Domain (3), Application (2), Controller (1)

### 3.10 Notificações (`domain.notification` / `application.notification`)
- **Regras por serviço**: `notification_rule` (FK `service_id` → catálogo `service`), evento (`VARCHAR(50)`), canal (`PUSH` | `EMAIL` | `SMS` | `WHATSAPP` | `IN_APP`), modelo de texto `template`, `active`. API: `/api/v1/notification-rules` — escrita (POST/PUT/DELETE) **apenas Supervisão**; listagem e GET por id: Operador ou Supervisão.
- **Registos**: `notification` (tenant, opcional `request_id`, `recipient_id`, canal, título, mensagem, `extra_data` JSONB, `status` `PENDING|SENT|FAILED|READ`, datas). API: `/api/v1/notifications` (criar, listar por `tenantId` + opcional `requestId`, GET por id, `PUT /{id}/status` para transições de estado).
- **Tests**: Domain (3), Application (4), Controller (4)

### Test Summary: **217 tests, 0 failures**

| Module | Tests |
|--------|-------|
| Domain | 99 |
| Application | 61 |
| Infrastructure | 57 (includes 2 integration tests; requires Docker) |
| **Total** | **217** |

---

## 4. Infrastructure & Configuration

### Docker Compose — deploy local (`backend/docker-compose.yml`)

Serviços: **PostGIS**, **Keycloak**, **API Spring Boot** (imagem multi-stage `Dockerfile` no mesmo diretório).

```bash
cd all-citizens/backend
docker compose up --build -d    # 5432 (DB), 9180 (Keycloak no host), 9080 (API)
```

- **Perfil `docker`**: `SPRING_PROFILES_ACTIVE=docker` — datasource `postgres`, JWKS em `http://keycloak:8080/...` (rede interna Compose), `issuer-uri` `http://localhost:9180/realms/allcitizens` (igual ao token obtido no browser). Ver `application-docker.yml`.
- **Volumes**: `pgdata` (BD), `attachments_data` → `/data/attachments` no contentor da app.
- **Postgres / Keycloak**: o Keycloak usa a base **`keycloak`** (`KC_DB_URL` → `jdbc:postgresql://postgres:5432/keycloak`), separada de **`allcitizens`** (Flyway da app). O contentor **`keycloak-db-init`** (imagem `postgres:16-alpine`) corre **uma vez** em cada `compose up`, depois do Postgres ficar saudável, e faz `CREATE DATABASE keycloak` se ainda não existir — assim funciona também com **volumes antigos**. O Keycloak só arranca após esse passo (`service_completed_successfully`; requer Docker Compose **v2.20+**). **WARN** do Quarkus/Jakarta na build do Keycloak são habituais. **Logs** `relation ... does not exist` no Postgres durante o primeiro Liquibase do Keycloak podem ser ignorados se o serviço terminar com *started*.
- **Health**: Postgres (`pg_isready`); app (`/actuator/health`). Keycloak arranca após Postgres saudável; a app depende de Keycloak **started** — na primeira subida pode levar ~1–2 min até o Keycloak responder; repetir pedido à API se falhar validação JWT momentaneamente.
- **Apenas dependências** (sem rebuild da API): `docker compose up -d postgres keycloak`.
- **Desenvolvimento na máquina** com DB/Keycloak nos contentores: manter `application.yml` com `localhost` para Postgres e JWT, como hoje.

### Spring Profiles
- **default**: Expects Keycloak running for JWT validation
- **dev**: Disables OAuth2/JWT, permits all requests (use for local development)
  - Config: `DevSecurityConfig.java` (active with `@Profile("dev")`)
  - Original `SecurityConfig.java` has `@Profile("!dev")`

### Keycloak — realm `allcitizens` (alinhado ao backend)

**Backend (já implementado, perfil `!dev`):**
- `issuer-uri` / `jwk-set-uri` em `application.yml`: `http://localhost:9180/realms/allcitizens` | servidor HTTP em `server.port: 9080`
- `KeycloakRealmRoleConverter` lê `realm_access.roles` e mapeia **apenas** as realm roles abaixo para `ROLE_OPERADOR_ATENDIMENTO` e `ROLE_SUPERVISAO` (outras roles do Keycloak são ignoradas).
- `SecurityConfig` aplica a matriz HTTP × papel (ver **HTTP API × papéis** abaixo). Constantes de nomes: `ApplicationRoles.java`.

### HTTP API × papéis (Spring `SecurityConfig`, perfil `!dev`)

Público: `GET /actuator/health`, `GET /api/v1/public/**`, **Swagger UI** e OpenAPI JSON (`/swagger-ui.html`, `/v3/api-docs`; ver springdoc em `application.yml`).

| Área | Operador de atendimento | Supervisão |
|------|-------------------------|------------|
| **Tenant** | GET (lista/detalhe) | POST, PUT, DELETE |
| **Subdivisões** (global) | GET | POST, PUT, DELETE |
| **Departamentos, Assuntos, Serviços do catálogo** | GET | POST, PUT, DELETE |
| **Tipos de histórico** (`/api/v1/history-types`) | GET | POST |
| **Motivos de encaminhamento** (`/api/v1/forwarding-reasons`) | GET | POST |
| **Pessoas, Endereços** | POST, GET, PUT; sem DELETE | Inclui DELETE |
| **Anexos** | POST, GET; sem DELETE | Inclui DELETE |
| **Pedidos, encaminhamentos, respostas, redistribuições, histórico do pedido** | Todas as operações expostas | Idem |
| **Auditoria** (`GET /api/v1/audit-logs`) | — | Apenas consulta (Supervisão) |
| **Regras de notificação** (`/api/v1/notification-rules`) | GET, listagem | POST, PUT, DELETE |

Qualquer outro caminho sob `/api/v1/**` segue a linha **Operador ou Supervisão** (ambos os papéis). Perfil **`dev`**: sem JWT (`DevSecurityConfig` — tudo permitido para testes locais).

**No Keycloak Admin (manual até automatizar):**
1. Criar realm **`allcitizens`** (ou usar o que já existir com esse nome).
2. Em **Realm roles**, criar exatamente:
   - `operador-atendimento`
   - `supervisao`
3. Atribuir essas roles aos **utilizadores** (ou a grupos) conforme o papel.
4. Criar **client** para a app (ex.: `allcitizens-api` confidencial ou público para SPA, conforme o front): configurar redirect URIs, obter tokens com **realm roles** no access token (por defeito `realm_access.roles` aparece no JWT do Keycloak).

### OpenAPI / Swagger UI (springdoc)

- Dependência: `springdoc-openapi-starter-webmvc-ui` no módulo `infrastructure`.
- Metadados e esquema **Bearer JWT**: `OpenApiConfiguration.java`.
- Com a app em execução (ex.: `http://localhost:9080`): abrir `http://localhost:9080/swagger-ui.html` → **Authorize** → colar o access token do Keycloak (ou `Bearer <token>` consoante o campo). Chamadas à API continuam a respeitar a matriz de papéis.

### Running the Application
```bash
cd all-citizens/backend
mvn install -DskipTests                                    # build all modules
mvn spring-boot:run -pl infrastructure -Dspring-boot.run.profiles=dev  # run with dev profile
```

### Running Tests
```bash
cd all-citizens/backend
mvn test   # runs all tests (unit + WebMvcTest + integration)
```

**Integration tests** (`InfrastructureApiIntegrationTest`): PostgreSQL via Testcontainers — **Docker must be running**. They boot the full Spring context, run Flyway (`V1` + newer migrations), and exercise HTTP APIs end-to-end (including multipart upload).

### Database
- **Flyway migrations** (never edit an already-applied version; add a new `V{n}__...sql` for any schema change):
  - `V1__initial_schema.sql` — full schema (50+ tables, ENUMs, triggers, indexes)
  - `V2__pg_trgm_service_request_search.sql` — GIN trigram indexes on `service_request.protocol` / `description` (extension + indexes may duplicate harmless parts of `V1` when `pg_trgm` already exists)
  - `V3__audit_log.sql` — tabela `audit_log` (trilha HTTP / RBAC)
- **JDBC URL quirk**: `?stringtype=unspecified` helps some bindings; for native PostgreSQL ENUM columns, JPA entities use `@JdbcType(PostgreSQLEnumJdbcType.class)` together with `@Enumerated(EnumType.STRING)` on enum fields (see `ServiceRequestJpaEntity`, `CatalogServiceJpaEntity`, forwarding/attachment entities).
- **Seed data required**: State/City/District tables need seed data for Address FKs. Current test data:
  ```sql
  INSERT INTO state (code, name) VALUES ('SP', 'São Paulo');
  INSERT INTO city (id, state_code, name, ibge_code) VALUES ('11111111-...', 'SP', 'São Paulo', '3550308');
  INSERT INTO district (id, city_id, name, official) VALUES ('22222222-...', '11111111-...', 'Sé', true);
  ```

---

## 5. Known Decisions & Trade-offs

1. **Java 17 instead of 21**: Local environment constraint. Project was designed for 21 but `pom.xml` was adjusted to 17.
2. **`hibernate.ddl-auto: none`**: Changed from `validate` because PostgreSQL `char(2)` (`bpchar`) caused validation mismatch. Flyway is the schema source of truth.
3. **`stringtype=unspecified` in JDBC URL**: Required for PostgreSQL custom ENUM types to work with Hibernate's string parameters.
4. **Person aggregate**: Combines `person` + `individual` tables into a single domain entity. The persistence layer handles the split. Organization type is not yet implemented.
5. **Subdivision has no tenant_id**: It's a global lookup table shared across tenants.
6. **PostgreSQL ENUM columns + Hibernate**: Native types (`priority_type`, `channel_type`, etc.) require `PostgreSQLEnumJdbcType` on JPA enum fields so inserts are not sent as plain `varchar` (integration tests against real PostgreSQL catch this).
7. **RBAC HTTP**: Operador foca atendimento; Supervisão gere estrutura (tenant, catálogos, subdivisão global) e remoções sensíveis. Ver secção *HTTP API × papéis*.
8. **Audit HTTP**: Registo por filtro após autenticação; mutações por defeito; leitura de audit só Supervisão. Falhas de persistência do audit não impedem a resposta HTTP (apenas log).

---

## 6. Existing API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/tenants` | Create tenant |
| GET | `/api/v1/tenants` | List all tenants |
| GET | `/api/v1/tenants/{id}` | Get tenant |
| PUT | `/api/v1/tenants/{id}` | Update tenant |
| DELETE | `/api/v1/tenants/{id}` | Delete tenant |
| POST | `/api/v1/departments` | Create department |
| GET | `/api/v1/departments?tenantId=` | List departments |
| GET | `/api/v1/departments/{id}` | Get department |
| PUT | `/api/v1/departments/{id}` | Update department |
| DELETE | `/api/v1/departments/{id}` | Delete department |
| POST | `/api/v1/subjects` | Create subject |
| GET | `/api/v1/subjects?tenantId=` | List subjects |
| GET | `/api/v1/subjects/{id}` | Get subject |
| PUT | `/api/v1/subjects/{id}` | Update subject |
| DELETE | `/api/v1/subjects/{id}` | Delete subject |
| POST | `/api/v1/subdivisions` | Create subdivision |
| GET | `/api/v1/subdivisions` | List all subdivisions |
| GET | `/api/v1/subdivisions/{id}` | Get subdivision |
| PUT | `/api/v1/subdivisions/{id}` | Update subdivision |
| DELETE | `/api/v1/subdivisions/{id}` | Delete subdivision |
| POST | `/api/v1/catalog-services` | Create catalog service |
| GET | `/api/v1/catalog-services?tenantId=` | List catalog services |
| GET | `/api/v1/catalog-services/{id}` | Get catalog service |
| PUT | `/api/v1/catalog-services/{id}` | Update catalog service |
| DELETE | `/api/v1/catalog-services/{id}` | Delete catalog service |
| POST | `/api/v1/addresses` | Create address |
| GET | `/api/v1/addresses/{id}` | Get address |
| PUT | `/api/v1/addresses/{id}` | Update address |
| DELETE | `/api/v1/addresses/{id}` | Delete address |
| POST | `/api/v1/persons` | Create person (INDIVIDUAL) |
| GET | `/api/v1/persons?tenantId=` | List persons |
| GET | `/api/v1/persons/{id}` | Get person |
| PUT | `/api/v1/persons/{id}` | Update person |
| DELETE | `/api/v1/persons/{id}` | Delete person |
| POST | `/api/v1/service-requests` | Create service request |
| GET | `/api/v1/service-requests?tenantId=` | List service requests |
| GET | `/api/v1/service-requests/{id}` | Get service request |
| PUT | `/api/v1/service-requests/{id}` | Update service request |
| POST | `/api/v1/service-requests/{id}/close` | Close service request |
| POST | `/api/v1/service-requests/{id}/cancel` | Cancel service request |
| GET | `/api/v1/service-requests?tenantId=&page=&size=&q=` | Paginated list + optional search (`q`) |
| GET/POST | `/api/v1/forwarding-reasons` | List / create forwarding reasons |
| GET/POST | `/api/v1/service-requests/{id}/forwardings` | List / create forwardings |
| GET | `/api/v1/forwardings/{id}` | Get forwarding |
| GET/POST | `/api/v1/forwardings/{id}/responses` | List / add forwarding response |
| GET/POST | `/api/v1/forwardings/{forwardingId}/redistributions` | List / create redistribution |
| GET/POST | `/api/v1/history-types` | List / create history types |
| GET/POST | `/api/v1/service-requests/{id}/history` | List / append request history |
| POST/GET/DELETE | `/api/v1/attachments` (multipart), `/api/v1/attachments/{id}`, `/api/v1/service-requests/{id}/attachments` | File upload and listing |
| GET | `/api/v1/audit-logs?page=&size=&from=&to=` | Lista registos de auditoria (Supervisão); `from`/`to` ISO-8601 opcionais |
| POST/GET | `/api/v1/notifications`, `GET /api/v1/notifications/{id}`, `PUT /api/v1/notifications/{id}/status` | Notificações por tenant / pedido; atualizar `PENDING`→`SENT`/`FAILED`, `SENT`→`READ` |
| POST/GET/PUT/DELETE | `/api/v1/notification-rules`, `GET ?serviceId=` | Regras por serviço do catálogo; escrita só Supervisão |
| GET | `/api/v1/tenants?page=&size=&q=`, `/api/v1/departments?...`, `/api/v1/persons?...`, `/api/v1/subjects?...`, `/api/v1/catalog-services?...`, `/api/v1/subdivisions?...` | Paginated list endpoints with optional `q` |

---

## 7. File Structure Pattern (per feature)

When adding a new feature, create files in this structure:

```
domain/src/main/java/com/allcitizens/domain/{feature}/
├── {Entity}.java                    # Domain entity
├── {Entity}Repository.java          # Repository port (interface)
└── {Enum}.java                      # Domain enums (if any)

domain/src/test/java/com/allcitizens/domain/{feature}/
└── {Entity}Test.java                # Domain unit tests

application/src/main/java/com/allcitizens/application/{feature}/
├── command/
│   ├── Create{Entity}Command.java   # Java record
│   └── Update{Entity}Command.java   # Java record
├── result/
│   └── {Entity}Result.java          # Java record + fromDomain()
├── usecase/
│   ├── Create{Entity}UseCase.java   # Functional interface
│   ├── Get{Entity}UseCase.java
│   ├── Update{Entity}UseCase.java
│   ├── List{Entities}UseCase.java
│   └── Delete{Entity}UseCase.java
└── service/
    └── {Entity}ApplicationService.java

application/src/test/java/com/allcitizens/application/{feature}/service/
└── {Entity}ApplicationServiceTest.java

infrastructure/src/main/java/com/allcitizens/infrastructure/adapter/
├── inbound/rest/{feature}/
│   ├── {Entity}Controller.java
│   ├── dto/
│   │   ├── Create{Entity}Request.java
│   │   ├── Update{Entity}Request.java
│   │   └── {Entity}Response.java
│   └── mapper/
│       └── {Entity}RestMapper.java
└── outbound/persistence/{feature}/
    ├── {Entity}RepositoryImpl.java
    ├── entity/
    │   └── {Entity}JpaEntity.java
    ├── mapper/
    │   └── {Entity}PersistenceMapper.java
    └── repository/
        └── Jpa{Entity}Repository.java

infrastructure/src/test/java/com/allcitizens/infrastructure/adapter/inbound/rest/{feature}/
└── {Entity}ControllerTest.java
```

After creating all files, **add beans to `BeanConfiguration.java`**.

---

## 8. Roadmap — What's Next

### Phase 2 — Backend Core (DONE)

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 1 | Service Catalog (Subject + Subdivision + Service) | DONE | |
| 2 | Person / Citizen | DONE | Individual type only |
| 3 | Address | DONE | |
| 4 | Request Forwarding | DONE | REST + domain + persistence; `forwarding`, `forwarding_reason`, `forwarding_response`, `redistribution` |
| 5 | Request History / Timeline | DONE | `request_history`, `history_type` |
| 6 | Attachments | DONE | Multipart upload, local storage `app.attachments.directory` |
| 7 | Pagination + Filtering + Search | DONE | `PageResult` / `PageResponse`, `q` on list endpoints; trigram search on service requests (`V2` + repository) |

### Phase 3 — Infrastructure & Integrations

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 1 | Keycloak Setup | IN PROGRESS | Backend: JWT + realm role mapping + HTTP rules (`SecurityConfig`, `KeycloakRealmRoleConverter`, `ApplicationRoles`). Admin: criar realm, roles `operador-atendimento` / `supervisao`, users, client para o front — ver secção *Keycloak — realm allcitizens*. |
| 2 | OpenAPI / Swagger | DONE | `springdoc-openapi-starter-webmvc-ui` 2.6.x; `OpenApiConfiguration`; Swagger/UI e `/v3/api-docs` públicos em `SecurityConfig` |
| 3 | Audit Log | DONE | `AuditLoggingFilter`, `audit_log`, `GET /api/v1/audit-logs` (Supervisão); `app.audit.log-get-requests` |
| 4 | Notification Service | DONE | JPA + REST: `notification`, `notification_rule`; envio real (push/SMS/etc.) fora de âmbito |
| 5 | Docker Compose completo | DONE | `docker-compose.yml`: Postgres + Keycloak + `app` (build `Dockerfile`); perfil `application-docker.yml`; volume anexos; healthchecks |

### Phase 4 — Frontend (Next.js)

| # | Feature | Status |
|---|---------|--------|
| 1 | Layout + Auth (Keycloak login) | TODO |
| 2 | Dashboard | TODO |
| 3 | CRUD de Solicitações | TODO |
| 4 | Portal do Cidadão | TODO |

### Phase 5 — Mobile

| # | Feature | Status |
|---|---------|--------|
| 1 | Citizen app (React Native or Flutter) | TODO |

---

## 9. DB Tables Not Yet Mapped to JPA

These tables exist in the database (created by `V1__initial_schema.sql`) but do **not** have corresponding JPA entities or application code yet (Phase 2 workflow tables **are** mapped; notificações — secção 3.10 — **já** mapeadas):

| Table | Purpose | Phase |
|-------|---------|-------|
| `phone` | Person phone numbers | Future |
| `document` | Person documents (RG, etc.) | Future |
| `document_type` | Document type lookup | Future |
| `phone_type` | Phone type lookup | Future |
| `organization` | Person subtype for companies | Future |
| `organization_contact` | Organization contacts | Future |
| `citizen_contact` | Citizen contact preferences | Future |
| `citizen_no_contact` | Citizen no-contact preferences | Future |
| `no_contact_type` | No-contact type lookup | Future |
| `question` | Dynamic questions per service | Future |
| `answer_option` | Answer options for questions | Future |
| `standard_description` | Template descriptions per service | Future |
| `response_method` | Response method lookup | Future |
| `response_evaluation` | Citizen satisfaction survey | Future |
| `survey` | Survey definitions | Future |
| `survey_response` | Survey answers | Future |
| `request_type` | Request type lookup | Future |
| `incident` | Incident tracking | Future |
| `urgency` | Urgency lookup | Future |
| `auto_closure` | Auto-closure rules | Future |
| `agent_task` | Agent task assignments | Future |
| `service_responsible` | Service responsible assignments | Future |
| `responsible_city` | Responsible by city | Future |
| `responsible_district` | Responsible by district | Future |
| `sla_escalation_level` | SLA escalation rules | Future |
| `sla_escalation_user` | SLA escalation contacts | Future |
| `email_log` | Email send log | Future |
| `no_service_record` | Records when no service found | Future |
| `not_found_type` | Not found type lookup | Future |
| `user_department` | User-department assignments | Future |
| `state` | State lookup (seeded) | Reference data |
| `city` | City lookup (seeded) | Reference data |
| `district` | District lookup (seeded) | Reference data |

---

## 10. Quick Commands Reference

```bash
# Start infrastructure
cd all-citizens/backend
docker compose up -d

# Build
mvn clean install -DskipTests

# Run tests
mvn test

# Run application (dev mode, no Keycloak required)
mvn spring-boot:run -pl infrastructure -Dspring-boot.run.profiles=dev

# Run application (production mode, requires Keycloak)
mvn spring-boot:run -pl infrastructure

# Check database
docker exec allcitizens-db psql -U allcitizens -d allcitizens -c "\dt"
```
