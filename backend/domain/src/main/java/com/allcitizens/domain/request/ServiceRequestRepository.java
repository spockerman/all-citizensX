package com.allcitizens.domain.request;

import com.allcitizens.domain.common.PageResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceRequestRepository {

    ServiceRequest save(ServiceRequest serviceRequest);

    Optional<ServiceRequest> findById(UUID id);

    Optional<ServiceRequest> findByTenantIdAndProtocol(UUID tenantId, String protocol);

    List<ServiceRequest> findAllByTenantId(UUID tenantId);

    PageResult<ServiceRequest> findAllByTenantIdPaged(UUID tenantId, int page, int size);

    PageResult<ServiceRequest> searchByTenantIdPaged(UUID tenantId, String query, int page, int size);

    void deleteById(UUID id);
}
