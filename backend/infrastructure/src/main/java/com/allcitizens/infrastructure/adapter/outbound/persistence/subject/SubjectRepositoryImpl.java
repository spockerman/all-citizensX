package com.allcitizens.infrastructure.adapter.outbound.persistence.subject;

import com.allcitizens.domain.common.PageResult;
import com.allcitizens.domain.subject.Subject;
import com.allcitizens.domain.subject.SubjectRepository;
import com.allcitizens.infrastructure.adapter.outbound.persistence.subject.entity.SubjectJpaEntity;
import com.allcitizens.infrastructure.adapter.outbound.persistence.subject.mapper.SubjectPersistenceMapper;
import com.allcitizens.infrastructure.adapter.outbound.persistence.subject.repository.JpaSubjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SubjectRepositoryImpl implements SubjectRepository {

    private final JpaSubjectRepository jpaRepository;
    private final SubjectPersistenceMapper mapper;

    public SubjectRepositoryImpl(JpaSubjectRepository jpaRepository,
                                 SubjectPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Subject save(Subject subject) {
        var entity = mapper.toEntity(subject);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Subject> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Subject> findAllByTenantId(UUID tenantId) {
        return jpaRepository.findAllByTenantId(tenantId).stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override
    public PageResult<Subject> findAllByTenantIdPaged(UUID tenantId, int page, int size) {
        return toPageResult(jpaRepository.findAllByTenantId(tenantId, PageRequest.of(page, size)));
    }

    @Override
    public PageResult<Subject> searchByTenantIdPaged(UUID tenantId, String query, int page, int size) {
        return toPageResult(jpaRepository.searchByTenantId(tenantId, query, PageRequest.of(page, size)));
    }

    private PageResult<Subject> toPageResult(Page<SubjectJpaEntity> page) {
        var content = page.getContent().stream().map(mapper::toDomain).toList();
        return new PageResult<>(content, page.getTotalElements(), page.getNumber(), page.getSize());
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
