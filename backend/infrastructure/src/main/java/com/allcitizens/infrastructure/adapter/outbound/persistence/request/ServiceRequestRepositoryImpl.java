package com.allcitizens.infrastructure.adapter.outbound.persistence.request;

import com.allcitizens.domain.common.PageResult;
import com.allcitizens.domain.request.ServiceRequest;
import com.allcitizens.domain.request.ServiceRequestRepository;
import com.allcitizens.infrastructure.adapter.outbound.persistence.request.mapper.ServiceRequestPersistenceMapper;
import com.allcitizens.infrastructure.adapter.outbound.persistence.request.repository.JpaServiceRequestRepository;
import com.allcitizens.infrastructure.adapter.outbound.persistence.request.entity.ServiceRequestJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ServiceRequestRepositoryImpl implements ServiceRequestRepository {

    private final JpaServiceRequestRepository jpaRepository;
    private final ServiceRequestPersistenceMapper mapper;

    public ServiceRequestRepositoryImpl(JpaServiceRequestRepository jpaRepository,
                                        ServiceRequestPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public ServiceRequest save(ServiceRequest serviceRequest) {
        var entity = mapper.toEntity(serviceRequest);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<ServiceRequest> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<ServiceRequest> findByTenantIdAndProtocol(UUID tenantId, String protocol) {
        return jpaRepository.findByTenantIdAndProtocol(tenantId, protocol).map(mapper::toDomain);
    }

    @Override
    public List<ServiceRequest> findAllByTenantId(UUID tenantId) {
        return jpaRepository.findAllByTenantId(tenantId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public PageResult<ServiceRequest> findAllByTenantIdPaged(UUID tenantId, int page, int size) {
        var pg = jpaRepository.findAllByTenantId(tenantId, PageRequest.of(page, size));
        return toPageResult(pg);
    }

    @Override
    public PageResult<ServiceRequest> searchByTenantIdPaged(UUID tenantId, String query, int page, int size) {
        var pg = jpaRepository.searchByTenantId(tenantId, query, PageRequest.of(page, size));
        return toPageResult(pg);
    }

    private PageResult<ServiceRequest> toPageResult(Page<ServiceRequestJpaEntity> pg) {
        var content = pg.getContent().stream().map(mapper::toDomain).toList();
        return new PageResult<>(content, pg.getTotalElements(), pg.getNumber(), pg.getSize());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
