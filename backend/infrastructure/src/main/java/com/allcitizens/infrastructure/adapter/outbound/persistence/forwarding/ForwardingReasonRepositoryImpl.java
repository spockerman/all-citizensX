package com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding;

import com.allcitizens.domain.forwarding.ForwardingReason;
import com.allcitizens.domain.forwarding.ForwardingReasonRepository;
import com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.mapper.ForwardingReasonPersistenceMapper;
import com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.repository.JpaForwardingReasonRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ForwardingReasonRepositoryImpl implements ForwardingReasonRepository {

    private final JpaForwardingReasonRepository jpa;
    private final ForwardingReasonPersistenceMapper mapper;

    public ForwardingReasonRepositoryImpl(JpaForwardingReasonRepository jpa,
                                          ForwardingReasonPersistenceMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public ForwardingReason save(ForwardingReason reason) {
        return mapper.toDomain(jpa.save(mapper.toEntity(reason)));
    }

    @Override
    public Optional<ForwardingReason> findById(UUID id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<ForwardingReason> findAll() {
        return jpa.findAll().stream().map(mapper::toDomain).toList();
    }
}
