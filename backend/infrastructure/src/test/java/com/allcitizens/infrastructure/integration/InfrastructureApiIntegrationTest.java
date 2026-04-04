package com.allcitizens.infrastructure.integration;

import com.allcitizens.infrastructure.adapter.inbound.rest.catalog.dto.CatalogServiceResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.catalog.dto.CreateCatalogServiceRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.common.dto.PageResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.department.dto.CreateDepartmentRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.department.dto.DepartmentResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.CreateForwardingReasonRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.CreateForwardingRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.ForwardingReasonResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.ForwardingResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.history.dto.AppendRequestHistoryRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.history.dto.CreateHistoryTypeRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.history.dto.HistoryTypeResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.history.dto.RequestHistoryResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.request.dto.CreateServiceRequestRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.request.dto.ServiceRequestResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.subject.dto.CreateSubjectRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.subject.dto.SubjectResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.subdivision.dto.CreateSubdivisionRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.subdivision.dto.SubdivisionResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.tenant.dto.CreateTenantRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.tenant.dto.TenantResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.attachment.dto.AttachmentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Full-stack tests: Spring context, Flyway (V1 + follow-up migrations), JPA, REST.
 * Requires Docker (Testcontainers).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@Testcontainers
class InfrastructureApiIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> POSTGRES.getJdbcUrl() + "?stringtype=unspecified");
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add(
                "app.attachments.directory",
                () -> System.getProperty("java.io.tmpdir") + "/allcitizens-integration-attachments");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void serviceRequestLifecycle_includesSearchForwardingHistoryAndAttachment() {
        String base = restTemplate.getRootUri();
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        var tenant = postJson(
                base + "/api/v1/tenants",
                new CreateTenantRequest("Integration Tenant " + suffix, "IT" + suffix.substring(0, 6)),
                TenantResponse.class);
        assertThat(tenant.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UUID tenantId = requireBody(tenant).id();

        var deptSource = postJson(
                base + "/api/v1/departments",
                new CreateDepartmentRequest(tenantId, null, "Dept Source " + suffix, null, null, true, true, null, 0),
                DepartmentResponse.class);
        var deptTarget = postJson(
                base + "/api/v1/departments",
                new CreateDepartmentRequest(tenantId, null, "Dept Target " + suffix, null, null, true, false, null, 1),
                DepartmentResponse.class);
        assertThat(deptSource.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(deptTarget.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UUID sourceDeptId = requireBody(deptSource).id();
        UUID targetDeptId = requireBody(deptTarget).id();

        var subdivision = postJson(
                base + "/api/v1/subdivisions",
                new CreateSubdivisionRequest("Sub " + suffix),
                SubdivisionResponse.class);
        assertThat(subdivision.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UUID subdivisionId = requireBody(subdivision).id();

        var subject = postJson(
                base + "/api/v1/subjects",
                new CreateSubjectRequest(tenantId, sourceDeptId, "Subject " + suffix, true, true),
                SubjectResponse.class);
        assertThat(subject.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UUID subjectId = requireBody(subject).id();

        var catalog = postJson(
                base + "/api/v1/catalog-services",
                new CreateCatalogServiceRequest(
                        tenantId,
                        subjectId,
                        subdivisionId,
                        sourceDeptId,
                        "Catalog " + suffix,
                        "Description",
                        30,
                        "NORMAL",
                        true,
                        true,
                        true,
                        true),
                CatalogServiceResponse.class);
        assertThat(catalog.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UUID serviceId = requireBody(catalog).id();

        String protocol = "PROT-IT-" + suffix;
        String searchToken = "trigramtoken" + suffix;
        var requestBody = new CreateServiceRequestRequest(
                tenantId,
                protocol,
                serviceId,
                null,
                null,
                "WEB",
                "NORMAL",
                "Relato com termo " + searchToken + " para busca",
                false,
                false,
                null,
                null,
                null);

        var createdRequest = postJson(base + "/api/v1/service-requests", requestBody, ServiceRequestResponse.class);
        assertThat(createdRequest.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UUID requestId = requireBody(createdRequest).id();

        String searchUrl = base + "/api/v1/service-requests?tenantId=" + tenantId + "&page=0&size=10&q=" + searchToken;
        ResponseEntity<PageResponse<ServiceRequestResponse>> searchPage = restTemplate.exchange(
                searchUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
        assertThat(searchPage.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(requireBody(searchPage).content()).hasSize(1);
        assertThat(requireBody(searchPage).content().get(0).protocol()).isEqualTo(protocol);

        var reason = postJson(
                base + "/api/v1/forwarding-reasons",
                new CreateForwardingReasonRequest("Reason " + suffix, "I"),
                ForwardingReasonResponse.class);
        assertThat(reason.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UUID reasonId = requireBody(reason).id();

        var forwarding = postJson(
                base + "/api/v1/service-requests/" + requestId + "/forwardings",
                new CreateForwardingRequest(targetDeptId, sourceDeptId, reasonId, null, "Encaminhado", null),
                ForwardingResponse.class);
        assertThat(forwarding.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(requireBody(forwarding).targetDepartmentId()).isEqualTo(targetDeptId);

        ResponseEntity<List<ForwardingResponse>> forwardings = restTemplate.exchange(
                base + "/api/v1/service-requests/" + requestId + "/forwardings",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
        assertThat(forwardings.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(requireBody(forwardings)).hasSize(1);

        var histType = postJson(
                base + "/api/v1/history-types",
                new CreateHistoryTypeRequest("Type " + suffix),
                HistoryTypeResponse.class);
        assertThat(histType.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UUID historyTypeId = requireBody(histType).id();

        var historyEntry = postJson(
                base + "/api/v1/service-requests/" + requestId + "/history",
                new AppendRequestHistoryRequest(historyTypeId, null, "Nota no histórico", null, null),
                RequestHistoryResponse.class);
        assertThat(historyEntry.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<List<RequestHistoryResponse>> history = restTemplate.exchange(
                base + "/api/v1/service-requests/" + requestId + "/history",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
        assertThat(history.getStatusCode()).isEqualTo(HttpStatus.OK);
        var historyBody = requireBody(history);
        assertThat(historyBody.stream().anyMatch(h -> "Nota no histórico".equals(h.description())))
                .isTrue();
        assertThat(historyBody.stream().anyMatch(h -> h.description() != null && h.description().contains("FORWARDED")))
                .isTrue();

        MultiValueMap<String, Object> multipart = new LinkedMultiValueMap<>();
        multipart.add(
                "file",
                new ByteArrayResource("conteúdo".getBytes(StandardCharsets.UTF_8)) {
                    @Override
                    public String getFilename() {
                        return "doc-" + suffix + ".txt";
                    }
                });
        var uploadHeaders = new org.springframework.http.HttpHeaders();
        uploadHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        String uploadUrl = base
                + "/api/v1/attachments?requestId="
                + requestId
                + "&type=DOCUMENT";
        var uploadEntity = new HttpEntity<>(multipart, uploadHeaders);
        ResponseEntity<AttachmentResponse> uploaded =
                restTemplate.postForEntity(uploadUrl, uploadEntity, AttachmentResponse.class);
        assertThat(uploaded.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(requireBody(uploaded).requestId()).isEqualTo(requestId);

        ResponseEntity<List<AttachmentResponse>> attachments = restTemplate.exchange(
                base + "/api/v1/service-requests/" + requestId + "/attachments",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
        assertThat(attachments.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(requireBody(attachments)).hasSize(1);
    }

    @Test
    void tenantList_supportsPaginationQuery() {
        String base = restTemplate.getRootUri();
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String code = "PG" + suffix.substring(0, 6);

        var created = postJson(
                base + "/api/v1/tenants",
                new CreateTenantRequest("Paged Tenant " + suffix, code),
                TenantResponse.class);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String url = base + "/api/v1/tenants?page=0&size=5&q=" + code;
        ResponseEntity<PageResponse<TenantResponse>> page = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertThat(page.getStatusCode()).isEqualTo(HttpStatus.OK);
        var body = requireBody(page);
        assertThat(body.content().stream().map(TenantResponse::code).anyMatch(c -> c.equals(code))).isTrue();
        assertThat(body.totalElements()).isGreaterThanOrEqualTo(1);
    }

    private static <T> T requireBody(ResponseEntity<T> response) {
        assertThat(response.getBody()).isNotNull();
        return response.getBody();
    }

    private <T> ResponseEntity<T> postJson(String url, Object body, Class<T> responseType) {
        var headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.postForEntity(url, new HttpEntity<>(body, headers), responseType);
    }
}
