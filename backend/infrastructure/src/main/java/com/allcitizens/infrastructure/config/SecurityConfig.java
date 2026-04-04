package com.allcitizens.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import com.allcitizens.infrastructure.adapter.inbound.web.AuditLoggingFilter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Autorização HTTP (perfil {@code !dev}). Ordem: regras mais específicas primeiro.
 * <p>
 * Resumo: <strong>Supervisão</strong> monopoliza escrita em tenant, subdivisions, departments, subjects,
 * catalog-services, POST em history-types e forwarding-reasons, e DELETE em persons, addresses e attachments.
 * <strong>Operador</strong> e Supervisão partilham o restante {@code /api/v1/**} (pedidos, encaminhamentos,
 * histórico operacional, leitura de catálogos, CRU de pessoa/endereço, upload de anexos).
 * <p>
 * Matriz detalhada: {@code PROJECT_STATUS.md} (secção API × papéis).
 */
@Configuration
@EnableWebSecurity
@Profile("!dev")
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationConverter jwtAuthenticationConverter,
            AuditLoggingFilter auditLoggingFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterAfter(auditLoggingFilter, BearerTokenAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers(
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/public/**").permitAll()

                // --- Apenas Supervisão: estrutura, catálogos mestres, remoções sensíveis ---
                .requestMatchers(HttpMethod.POST, "/api/v1/tenants", "/api/v1/tenants/**")
                    .hasRole("SUPERVISAO")
                .requestMatchers(HttpMethod.PUT, "/api/v1/tenants", "/api/v1/tenants/**")
                    .hasRole("SUPERVISAO")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/tenants", "/api/v1/tenants/**")
                    .hasRole("SUPERVISAO")

                .requestMatchers(HttpMethod.POST, "/api/v1/subdivisions", "/api/v1/subdivisions/**")
                    .hasRole("SUPERVISAO")
                .requestMatchers(HttpMethod.PUT, "/api/v1/subdivisions", "/api/v1/subdivisions/**")
                    .hasRole("SUPERVISAO")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/subdivisions", "/api/v1/subdivisions/**")
                    .hasRole("SUPERVISAO")

                .requestMatchers(HttpMethod.POST, "/api/v1/departments", "/api/v1/departments/**")
                    .hasRole("SUPERVISAO")
                .requestMatchers(HttpMethod.PUT, "/api/v1/departments", "/api/v1/departments/**")
                    .hasRole("SUPERVISAO")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/departments", "/api/v1/departments/**")
                    .hasRole("SUPERVISAO")

                .requestMatchers(HttpMethod.POST, "/api/v1/subjects", "/api/v1/subjects/**")
                    .hasRole("SUPERVISAO")
                .requestMatchers(HttpMethod.PUT, "/api/v1/subjects", "/api/v1/subjects/**")
                    .hasRole("SUPERVISAO")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/subjects", "/api/v1/subjects/**")
                    .hasRole("SUPERVISAO")

                .requestMatchers(HttpMethod.POST, "/api/v1/catalog-services", "/api/v1/catalog-services/**")
                    .hasRole("SUPERVISAO")
                .requestMatchers(HttpMethod.PUT, "/api/v1/catalog-services", "/api/v1/catalog-services/**")
                    .hasRole("SUPERVISAO")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/catalog-services", "/api/v1/catalog-services/**")
                    .hasRole("SUPERVISAO")

                .requestMatchers(HttpMethod.POST, "/api/v1/history-types", "/api/v1/history-types/**")
                    .hasRole("SUPERVISAO")
                .requestMatchers(HttpMethod.POST, "/api/v1/forwarding-reasons", "/api/v1/forwarding-reasons/**")
                    .hasRole("SUPERVISAO")

                .requestMatchers(HttpMethod.POST, "/api/v1/notification-rules", "/api/v1/notification-rules/**")
                    .hasRole("SUPERVISAO")
                .requestMatchers(HttpMethod.PUT, "/api/v1/notification-rules", "/api/v1/notification-rules/**")
                    .hasRole("SUPERVISAO")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/notification-rules", "/api/v1/notification-rules/**")
                    .hasRole("SUPERVISAO")

                .requestMatchers(HttpMethod.DELETE, "/api/v1/persons", "/api/v1/persons/**")
                    .hasRole("SUPERVISAO")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/addresses", "/api/v1/addresses/**")
                    .hasRole("SUPERVISAO")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/attachments", "/api/v1/attachments/**")
                    .hasRole("SUPERVISAO")

                .requestMatchers(HttpMethod.GET, "/api/v1/audit-logs", "/api/v1/audit-logs/**")
                    .hasRole("SUPERVISAO")

                // --- Operador ou Supervisão: atendimento e leitura de catálogos ---
                .requestMatchers("/api/v1/**")
                    .hasAnyRole("OPERADOR_ATENDIMENTO", "SUPERVISAO")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                    jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));

        return http.build();
    }
}
