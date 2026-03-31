package com.allcitizens.domain.request;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceRequestRepository {

    ServiceRequest save(ServiceRequest serviceRequest);

    Optional<ServiceRequest> findById(UUID id);

    Optional<ServiceRequest> findByTenantIdAndProtocol(UUID tenantId, String protocol);

    List<ServiceRequest> findAllByTenantId(UUID tenantId);

    void deleteById(UUID id);
}
