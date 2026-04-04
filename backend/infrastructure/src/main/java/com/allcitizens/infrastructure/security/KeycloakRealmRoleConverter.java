package com.allcitizens.infrastructure.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Maps Keycloak access-token claim {@code realm_access.roles} to Spring authorities.
 * Only realm roles listed in the mapping are granted — other Keycloak defaults
 * ({@code offline_access}, etc.) are ignored.
 */
public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final Map<String, String> KEYCLOAK_REALM_TO_AUTHORITY = Map.of(
            ApplicationRoles.KEYCLOAK_OPERADOR_ATENDIMENTO, ApplicationRoles.OPERADOR_ATENDIMENTO,
            ApplicationRoles.KEYCLOAK_SUPERVISAO, ApplicationRoles.SUPERVISAO
    );

    @Override
    public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
        Set<GrantedAuthority> result = new HashSet<>();
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess == null) {
            return result;
        }
        Object rolesObj = realmAccess.get("roles");
        if (!(rolesObj instanceof Collection<?> roles)) {
            return result;
        }
        for (Object r : roles) {
            if (r instanceof String realmRole) {
                String authority = KEYCLOAK_REALM_TO_AUTHORITY.get(realmRole);
                if (authority != null) {
                    result.add(new SimpleGrantedAuthority(authority));
                }
            }
        }
        return result;
    }
}
