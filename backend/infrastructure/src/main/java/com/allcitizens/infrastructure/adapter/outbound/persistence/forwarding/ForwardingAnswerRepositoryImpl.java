package com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding;

import com.allcitizens.domain.forwarding.ForwardingAnswer;
import com.allcitizens.domain.forwarding.ForwardingAnswerRepository;
import com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.mapper.ForwardingAnswerPersistenceMapper;
import com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.repository.JpaForwardingAnswerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ForwardingAnswerRepositoryImpl implements ForwardingAnswerRepository {

    private final JpaForwardingAnswerRepository jpa;
    private final ForwardingAnswerPersistenceMapper mapper;

    public ForwardingAnswerRepositoryImpl(JpaForwardingAnswerRepository jpa,
                                          ForwardingAnswerPersistenceMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public ForwardingAnswer save(ForwardingAnswer answer) {
        return mapper.toDomain(jpa.save(mapper.toEntity(answer)));
    }

    @Override
    public List<ForwardingAnswer> findAllByForwardingId(UUID forwardingId) {
        return jpa.findAllByForwardingIdOrderByCreatedAtAsc(forwardingId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
