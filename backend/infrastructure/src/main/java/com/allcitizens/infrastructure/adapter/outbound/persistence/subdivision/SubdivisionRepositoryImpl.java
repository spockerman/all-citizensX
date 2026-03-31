package com.allcitizens.infrastructure.adapter.outbound.persistence.subdivision;

import com.allcitizens.domain.subdivision.Subdivision;
import com.allcitizens.domain.subdivision.SubdivisionRepository;
import com.allcitizens.infrastructure.adapter.outbound.persistence.subdivision.mapper.SubdivisionPersistenceMapper;
import com.allcitizens.infrastructure.adapter.outbound.persistence.subdivision.repository.JpaSubdivisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SubdivisionRepositoryImpl implements SubdivisionRepository {

    private final JpaSubdivisionRepository jpaRepository;
    private final SubdivisionPersistenceMapper mapper;

    public SubdivisionRepositoryImpl(JpaSubdivisionRepository jpaRepository,
                                     SubdivisionPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Subdivision save(Subdivision subdivision) {
        var entity = mapper.toEntity(subdivision);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Subdivision> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Subdivision> findAll() {
        return jpaRepository.findAll().stream()
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
