package com.allcitizens.application.audit.service;

import com.allcitizens.application.audit.query.ListAuditLogsQuery;
import com.allcitizens.application.audit.result.AuditLogResult;
import com.allcitizens.application.audit.usecase.ListAuditLogsUseCase;
import com.allcitizens.domain.audit.AuditLogEntry;
import com.allcitizens.domain.audit.AuditLogRepository;
import com.allcitizens.domain.common.PageResult;
import jakarta.transaction.Transactional;

import java.time.Instant;

public class AuditLogApplicationService implements ListAuditLogsUseCase {

    private final AuditLogRepository auditLogRepository;

    public AuditLogApplicationService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    /** Persists one entry; runs in own transaction when called from the servlet filter. */
    @Transactional
    public void record(AuditLogEntry entry) {
        auditLogRepository.save(entry);
    }

    @Override
    @Transactional
    public PageResult<AuditLogResult> execute(ListAuditLogsQuery query) {
        var safeSize = Math.min(Math.max(query.size(), 1), 100);
        var page = auditLogRepository.findAllPaged(query.page(), safeSize, query.fromInclusive(), query.toInclusive());
        var mapped = page.content().stream().map(AuditLogResult::fromDomain).toList();
        return new PageResult<>(mapped, page.totalElements(), page.page(), page.size());
    }
}
