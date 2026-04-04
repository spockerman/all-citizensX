package com.allcitizens.domain.audit;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuditLogEntryTest {

    @Test
    void createTruncatesLongPath() {
        var longPath = "x".repeat(2500);
        var e = AuditLogEntry.create(
                Instant.parse("2026-01-01T00:00:00Z"),
                "sub",
                "user",
                "OPERADOR_ATENDIMENTO",
                "POST",
                longPath,
                "127.0.0.1",
                201,
                "corr");
        assertThat(e.getRequestPath()).hasSize(2000);
    }

    @Test
    void reconstituteRoundTrip() {
        var id = UUID.randomUUID();
        var at = Instant.now();
        var e = AuditLogEntry.reconstitute(
                id, at, "s", "u", "roles", "DELETE", "/api/v1/x", "10.0.0.1", 204, "c");
        assertThat(e.getId()).isEqualTo(id);
        assertThat(e.getHttpMethod()).isEqualTo("DELETE");
    }

    @Test
    void createRejectsBlankMethod() {
        assertThatThrownBy(() -> AuditLogEntry.create(
                        Instant.now(), null, null, null, "  ", "/p", null, 200, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
