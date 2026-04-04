package com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding;

import com.allcitizens.domain.forwarding.Redistribution;
import com.allcitizens.domain.forwarding.RedistributionRepository;
import com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.mapper.RedistributionPersistenceMapper;
import com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.repository.JpaRedistributionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class RedistributionRepositoryImpl implements RedistributionRepository {

    private final JpaRedistributionRepository jpa;
    private final RedistributionPersistenceMapper mapper;

    public RedistributionRepositoryImpl(JpaRedistributionRepository jpa,
                                        RedistributionPersistenceMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Redistribution save(Redistribution redistribution) {
        return mapper.toDomain(jpa.save(mapper.toEntity(redistribution)));
    }

    @Override
    public List<Redistribution> findAllByForwardingId(UUID forwardingId) {
        return jpa.findAllByForwardingIdOrderByCreatedAtAsc(forwardingId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
