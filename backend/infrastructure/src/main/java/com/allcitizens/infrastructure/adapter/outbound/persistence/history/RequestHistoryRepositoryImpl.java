package com.allcitizens.infrastructure.adapter.outbound.persistence.history;

import com.allcitizens.domain.history.RequestHistory;
import com.allcitizens.domain.history.RequestHistoryRepository;
import com.allcitizens.infrastructure.adapter.outbound.persistence.history.mapper.RequestHistoryPersistenceMapper;
import com.allcitizens.infrastructure.adapter.outbound.persistence.history.repository.JpaRequestHistoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class RequestHistoryRepositoryImpl implements RequestHistoryRepository {

    private final JpaRequestHistoryRepository jpa;
    private final RequestHistoryPersistenceMapper mapper;

    public RequestHistoryRepositoryImpl(JpaRequestHistoryRepository jpa,
                                        RequestHistoryPersistenceMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public RequestHistory save(RequestHistory history) {
        return mapper.toDomain(jpa.save(mapper.toEntity(history)));
    }

    @Override
    public List<RequestHistory> findAllByRequestIdOrderByCreatedAtDesc(UUID requestId) {
        return jpa.findAllByRequestIdOrderByCreatedAtDesc(requestId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
