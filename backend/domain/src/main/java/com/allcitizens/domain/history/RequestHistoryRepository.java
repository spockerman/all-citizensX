package com.allcitizens.domain.history;

import java.util.List;
import java.util.UUID;

public interface RequestHistoryRepository {

    RequestHistory save(RequestHistory history);

    List<RequestHistory> findAllByRequestIdOrderByCreatedAtDesc(UUID requestId);
}
