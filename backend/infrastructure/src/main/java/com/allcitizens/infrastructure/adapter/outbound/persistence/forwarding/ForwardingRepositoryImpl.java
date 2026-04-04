package com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding;

import com.allcitizens.domain.forwarding.Forwarding;
import com.allcitizens.domain.forwarding.ForwardingRepository;
import com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.mapper.ForwardingPersistenceMapper;
import com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.repository.JpaForwardingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ForwardingRepositoryImpl implements ForwardingRepository {

    private final JpaForwardingRepository jpa;
    private final ForwardingPersistenceMapper mapper;

    public ForwardingRepositoryImpl(JpaForwardingRepository jpa, ForwardingPersistenceMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Forwarding save(Forwarding forwarding) {
        return mapper.toDomain(jpa.save(mapper.toEntity(forwarding)));
    }

    @Override
    public Optional<Forwarding> findById(UUID id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Forwarding> findAllByRequestId(UUID requestId) {
        return jpa.findAllByRequestIdOrderByCreatedAtDesc(requestId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
