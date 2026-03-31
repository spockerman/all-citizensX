package com.allcitizens.infrastructure.adapter.outbound.persistence.tenant;

import com.allcitizens.domain.tenant.Tenant;
import com.allcitizens.domain.tenant.TenantRepository;
import com.allcitizens.infrastructure.adapter.outbound.persistence.tenant.mapper.TenantPersistenceMapper;
import com.allcitizens.infrastructure.adapter.outbound.persistence.tenant.repository.JpaTenantRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TenantRepositoryImpl implements TenantRepository {

    private final JpaTenantRepository jpaRepository;
    private final TenantPersistenceMapper mapper;

    public TenantRepositoryImpl(JpaTenantRepository jpaRepository,
                                TenantPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Tenant save(Tenant tenant) {
        var entity = mapper.toEntity(tenant);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Tenant> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Tenant> findByCode(String code) {
        return jpaRepository.findByCode(code).map(mapper::toDomain);
    }

    @Override
    public List<Tenant> findAll() {
        return jpaRepository.findAll().stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
