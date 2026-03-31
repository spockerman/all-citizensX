package com.allcitizens.domain.tenant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantRepository {

    Tenant save(Tenant tenant);

    Optional<Tenant> findById(UUID id);

    Optional<Tenant> findByCode(String code);

    List<Tenant> findAll();

    boolean existsByCode(String code);

    void deleteById(UUID id);
}
