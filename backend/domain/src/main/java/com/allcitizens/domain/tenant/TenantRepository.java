package com.allcitizens.domain.tenant;

import com.allcitizens.domain.common.PageResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantRepository {

    Tenant save(Tenant tenant);

    Optional<Tenant> findById(UUID id);

    Optional<Tenant> findByCode(String code);

    List<Tenant> findAll();

    PageResult<Tenant> findAllPaged(int page, int size);

    PageResult<Tenant> searchPaged(String query, int page, int size);

    boolean existsByCode(String code);

    void deleteById(UUID id);
}
