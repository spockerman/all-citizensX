package com.allcitizens.infrastructure.adapter.outbound.persistence.catalog;

import com.allcitizens.domain.catalog.CatalogService;
import com.allcitizens.domain.catalog.CatalogServiceRepository;
import com.allcitizens.infrastructure.adapter.outbound.persistence.catalog.mapper.CatalogServicePersistenceMapper;
import com.allcitizens.infrastructure.adapter.outbound.persistence.catalog.repository.JpaCatalogServiceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CatalogServiceRepositoryImpl implements CatalogServiceRepository {

    private final JpaCatalogServiceRepository jpaRepository;
    private final CatalogServicePersistenceMapper mapper;

    public CatalogServiceRepositoryImpl(JpaCatalogServiceRepository jpaRepository,
                                        CatalogServicePersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public CatalogService save(CatalogService catalogService) {
        var entity = mapper.toEntity(catalogService);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<CatalogService> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<CatalogService> findAllByTenantId(UUID tenantId) {
        return jpaRepository.findAllByTenantId(tenantId).stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override
    public List<CatalogService> findAllBySubjectId(UUID subjectId) {
        return jpaRepository.findAllBySubjectId(subjectId).stream()
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
