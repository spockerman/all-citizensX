package com.allcitizens.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    static final String BEARER_JWT = "bearer-jwt";

    @Bean
    public OpenAPI allCitizensOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("All Citizens API")
                        .version("1.0")
                        .description("""
                                REST API for municipal citizen service requests (ombudsman-style platform). \
                                Most endpoints require a Keycloak **access token** for realm `allcitizens` \
                                with realm roles `operador-atendimento` and/or `supervisao`. \
                                Use **Authorize** and paste `Bearer <token>`, or only the raw JWT."""))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_JWT))
                .components(new Components()
                        .addSecuritySchemes(BEARER_JWT,
                                new SecurityScheme()
                                        .name(BEARER_JWT)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Keycloak access token (iss: realm allcitizens)")));
    }
}
