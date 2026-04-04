package com.allcitizens.infrastructure.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class KeycloakRealmRoleConverterTest {

    private final KeycloakRealmRoleConverter converter = new KeycloakRealmRoleConverter();

    @Test
    void mapsKnownRealmRolesAndIgnoresOthers() {
        Instant now = Instant.now();
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("realm_access", Map.of(
                        "roles", List.of(
                                ApplicationRoles.KEYCLOAK_OPERADOR_ATENDIMENTO,
                                "offline_access",
                                ApplicationRoles.KEYCLOAK_SUPERVISAO)))
                .issuedAt(now)
                .expiresAt(now.plusSeconds(120))
                .build();

        var authorities = converter.convert(jwt);

        assertThat(authorities)
                .extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder(
                        ApplicationRoles.OPERADOR_ATENDIMENTO,
                        ApplicationRoles.SUPERVISAO);
    }

    @Test
    void emptyWhenNoRealmAccess() {
        Instant now = Instant.now();
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(120))
                .build();

        assertThat(converter.convert(jwt)).isEmpty();
    }
}
