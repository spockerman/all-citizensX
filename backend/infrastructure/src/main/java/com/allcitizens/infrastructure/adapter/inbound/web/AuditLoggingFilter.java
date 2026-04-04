package com.allcitizens.infrastructure.adapter.inbound.web;

import com.allcitizens.application.audit.service.AuditLogApplicationService;
import com.allcitizens.domain.audit.AuditLogEntry;
import com.allcitizens.infrastructure.config.AuditProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.stream.Collectors;

public class AuditLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(AuditLoggingFilter.class);

    private final AuditLogApplicationService auditLogApplicationService;
    private final AuditProperties auditProperties;

    public AuditLoggingFilter(
            AuditLogApplicationService auditLogApplicationService,
            AuditProperties auditProperties) {
        this.auditLogApplicationService = auditLogApplicationService;
        this.auditProperties = auditProperties;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (!uri.startsWith("/api/v1/") || uri.startsWith("/api/v1/public/")) {
            return true;
        }
        if ("GET".equalsIgnoreCase(request.getMethod()) && !auditProperties.isLogGetRequests()) {
            return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } finally {
            try {
                persistAudit(request, response);
            } catch (Exception ex) {
                log.warn("Could not persist audit log entry", ex);
            }
        }
    }

    private void persistAudit(HttpServletRequest request, HttpServletResponse response) {
        String subject = null;
        String username = null;
        String roles = null;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken)) {
            if (auth.getPrincipal() instanceof Jwt jwt) {
                subject = jwt.getSubject();
                username = jwt.getClaimAsString("preferred_username");
                if (username == null || username.isBlank()) {
                    username = jwt.getClaimAsString("email");
                }
            }
            roles = auth.getAuthorities().stream()
                    .map(a -> a.getAuthority().replaceFirst("^ROLE_", ""))
                    .sorted()
                    .collect(Collectors.joining(","));
        }

        var method = request.getMethod();
        var path = request.getRequestURI();
        var query = request.getQueryString();
        if (query != null && !query.isBlank()) {
            path = path + "?" + query;
        }

        var correlationId = firstHeader(request, "X-Correlation-Id", "X-Request-Id");
        int status = response.getStatus();
        if (status == 0) {
            status = 200;
        }

        var entry = AuditLogEntry.create(
                Instant.now(),
                subject,
                username,
                roles,
                method,
                path,
                clientIp(request),
                status,
                correlationId);

        auditLogApplicationService.record(entry);
    }

    private static String firstHeader(HttpServletRequest request, String... names) {
        for (String name : names) {
            var v = request.getHeader(name);
            if (v != null && !v.isBlank()) {
                return v.trim();
            }
        }
        return null;
    }

    private static String clientIp(HttpServletRequest request) {
        var xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        var rip = request.getHeader("X-Real-IP");
        if (rip != null && !rip.isBlank()) {
            return rip.trim();
        }
        return request.getRemoteAddr();
    }
}
