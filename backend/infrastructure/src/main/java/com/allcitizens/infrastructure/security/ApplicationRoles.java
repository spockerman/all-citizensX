package com.allcitizens.infrastructure.security;

/**
 * Application roles aligned with Keycloak <strong>realm roles</strong> (names with hyphen)
 * and Spring Security {@code GrantedAuthority} values ({@code ROLE_*}).
 * <p>
 * In Keycloak Admin: create realm {@code allcitizens}, add realm roles exactly as
 * {@link #KEYCLOAK_OPERADOR_ATENDIMENTO} and {@link #KEYCLOAK_SUPERVISAO}, assign to users or groups.
 */
public final class ApplicationRoles {

    private ApplicationRoles() {
    }

    /** Keycloak realm role — Operador de Atendimento */
    public static final String KEYCLOAK_OPERADOR_ATENDIMENTO = "operador-atendimento";

    /** Keycloak realm role — Supervisão */
    public static final String KEYCLOAK_SUPERVISAO = "supervisao";

    /**
     * Spring Security authority used in {@code hasRole("OPERADOR_ATENDIMENTO")}
     * and {@code @PreAuthorize("hasRole('OPERADOR_ATENDIMENTO')")}.
     */
    public static final String OPERADOR_ATENDIMENTO = "ROLE_OPERADOR_ATENDIMENTO";

    /** Spring Security authority for {@code hasRole("SUPERVISAO")}. */
    public static final String SUPERVISAO = "ROLE_SUPERVISAO";
}
