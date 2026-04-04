package com.allcitizens.application.audit.service;

import com.allcitizens.application.audit.query.ListAuditLogsQuery;
import com.allcitizens.domain.audit.AuditLogEntry;
import com.allcitizens.domain.audit.AuditLogRepository;
import com.allcitizens.domain.common.PageResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditLogApplicationServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private AuditLogApplicationService service;

    @Test
    void recordDelegatesToRepository() {
        var entry = AuditLogEntry.create(
                Instant.now(), "sub", "alice", "SUPERVISAO", "PUT", "/api/v1/x", "127.0.0.1", 200, null);
        service.record(entry);
        var captor = ArgumentCaptor.forClass(AuditLogEntry.class);
        verify(auditLogRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(entry.getId());
    }

    @Test
    void listMapsResults() {
        var e = AuditLogEntry.reconstitute(
                java.util.UUID.randomUUID(),
                Instant.now(),
                "s",
                "u",
                "r",
                "POST",
                "/p",
                null,
                201,
                null);
        when(auditLogRepository.findAllPaged(0, 20, null, null))
                .thenReturn(new PageResult<>(List.of(e), 1, 0, 20));
        var page = service.execute(new ListAuditLogsQuery(0, 20, null, null));
        assertThat(page.content()).hasSize(1);
        assertThat(page.content().get(0).httpMethod()).isEqualTo("POST");
    }
}
