package com.allcitizens.infrastructure.adapter.outbound.persistence.audit;

import com.allcitizens.domain.audit.AuditLogEntry;
import com.allcitizens.domain.audit.AuditLogRepository;
import com.allcitizens.domain.common.PageResult;
import com.allcitizens.infrastructure.adapter.outbound.persistence.audit.mapper.AuditLogPersistenceMapper;
import com.allcitizens.infrastructure.adapter.outbound.persistence.audit.repository.JpaAuditLogRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public class AuditLogRepositoryImpl implements AuditLogRepository {

    private final JpaAuditLogRepository jpaRepository;
    private final AuditLogPersistenceMapper mapper;

    public AuditLogRepositoryImpl(JpaAuditLogRepository jpaRepository, AuditLogPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(AuditLogEntry entry) {
        jpaRepository.save(mapper.toEntity(entry));
    }

    @Override
    public PageResult<AuditLogEntry> findAllPaged(int page, int size, Instant fromInclusive, Instant toInclusive) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "occurredAt"));
        var result = jpaRepository.findFiltered(fromInclusive, toInclusive, pageable);
        var content = result.getContent().stream().map(mapper::toDomain).toList();
        return new PageResult<>(content, result.getTotalElements(), result.getNumber(), result.getSize());
    }
}
