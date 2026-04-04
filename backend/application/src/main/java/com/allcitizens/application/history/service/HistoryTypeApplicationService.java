package com.allcitizens.application.history.service;

import com.allcitizens.application.history.command.CreateHistoryTypeCommand;
import com.allcitizens.application.history.result.HistoryTypeResult;
import com.allcitizens.application.history.usecase.CreateHistoryTypeUseCase;
import com.allcitizens.application.history.usecase.ListHistoryTypesUseCase;
import com.allcitizens.domain.history.HistoryType;
import com.allcitizens.domain.history.HistoryTypeRepository;
import jakarta.transaction.Transactional;

import java.util.List;

public class HistoryTypeApplicationService
        implements CreateHistoryTypeUseCase, ListHistoryTypesUseCase {

    private final HistoryTypeRepository repository;

    public HistoryTypeApplicationService(HistoryTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public HistoryTypeResult execute(CreateHistoryTypeCommand command) {
        var type = HistoryType.create(command.name());
        var saved = repository.save(type);
        return HistoryTypeResult.fromDomain(saved);
    }

    @Override
    @Transactional
    public List<HistoryTypeResult> execute() {
        return repository.findAll().stream()
                .map(HistoryTypeResult::fromDomain)
                .toList();
    }
}
