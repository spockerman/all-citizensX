package com.allcitizens.infrastructure.adapter.outbound.persistence.history;

import com.allcitizens.domain.history.HistoryType;
import com.allcitizens.domain.history.HistoryTypeRepository;
import com.allcitizens.infrastructure.adapter.outbound.persistence.history.mapper.HistoryTypePersistenceMapper;
import com.allcitizens.infrastructure.adapter.outbound.persistence.history.repository.JpaHistoryTypeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class HistoryTypeRepositoryImpl implements HistoryTypeRepository {

    private final JpaHistoryTypeRepository jpa;
    private final HistoryTypePersistenceMapper mapper;

    public HistoryTypeRepositoryImpl(JpaHistoryTypeRepository jpa, HistoryTypePersistenceMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public HistoryType save(HistoryType historyType) {
        return mapper.toDomain(jpa.save(mapper.toEntity(historyType)));
    }

    @Override
    public Optional<HistoryType> findById(UUID id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<HistoryType> findAll() {
        return jpa.findAll().stream().map(mapper::toDomain).toList();
    }
}
