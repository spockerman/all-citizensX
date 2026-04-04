package com.allcitizens.domain.history;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HistoryTypeRepository {

    HistoryType save(HistoryType historyType);

    Optional<HistoryType> findById(UUID id);

    List<HistoryType> findAll();
}
