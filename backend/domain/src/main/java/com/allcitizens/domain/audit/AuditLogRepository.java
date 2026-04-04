package com.allcitizens.domain.audit;

import com.allcitizens.domain.common.PageResult;

import java.time.Instant;

public interface AuditLogRepository {

    void save(AuditLogEntry entry);

    PageResult<AuditLogEntry> findAllPaged(int page, int size, Instant fromInclusive, Instant toInclusive);
}
