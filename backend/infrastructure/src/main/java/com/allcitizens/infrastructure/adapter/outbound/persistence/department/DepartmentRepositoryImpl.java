package com.allcitizens.infrastructure.adapter.outbound.persistence.department;

import com.allcitizens.domain.common.PageResult;
import com.allcitizens.domain.department.Department;
import com.allcitizens.domain.department.DepartmentRepository;
import com.allcitizens.infrastructure.adapter.outbound.persistence.department.entity.DepartmentJpaEntity;
import com.allcitizens.infrastructure.adapter.outbound.persistence.department.mapper.DepartmentPersistenceMapper;
import com.allcitizens.infrastructure.adapter.outbound.persistence.department.repository.JpaDepartmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final JpaDepartmentRepository jpaRepository;
    private final DepartmentPersistenceMapper mapper;

    public DepartmentRepositoryImpl(JpaDepartmentRepository jpaRepository,
                                    DepartmentPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Department save(Department department) {
        var entity = mapper.toEntity(department);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Department> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Department> findAllByTenantId(UUID tenantId) {
        return jpaRepository.findAllByTenantId(tenantId).stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override
    public PageResult<Department> findAllByTenantIdPaged(UUID tenantId, int page, int size) {
        return toPageResult(jpaRepository.findAllByTenantId(tenantId, PageRequest.of(page, size)));
    }

    @Override
    public PageResult<Department> searchByTenantIdPaged(UUID tenantId, String query, int page, int size) {
        return toPageResult(jpaRepository.searchByTenantId(tenantId, query, PageRequest.of(page, size)));
    }

    private PageResult<Department> toPageResult(Page<DepartmentJpaEntity> page) {
        var content = page.getContent().stream().map(mapper::toDomain).toList();
        return new PageResult<>(content, page.getTotalElements(), page.getNumber(), page.getSize());
    }

    @Override
    public List<Department> findByTenantIdAndParentIdIsNull(UUID tenantId) {
        return jpaRepository.findByTenantIdAndParentIdIsNull(tenantId).stream()
            .map(mapper::toDomain)
            .toList();
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
