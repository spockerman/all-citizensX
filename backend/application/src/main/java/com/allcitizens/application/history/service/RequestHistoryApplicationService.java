package com.allcitizens.application.history.service;

import com.allcitizens.application.history.command.AppendRequestHistoryCommand;
import com.allcitizens.application.history.result.RequestHistoryResult;
import com.allcitizens.application.history.usecase.AppendRequestHistoryUseCase;
import com.allcitizens.application.history.usecase.ListRequestHistoryUseCase;
import com.allcitizens.domain.exception.EntityNotFoundException;
import com.allcitizens.domain.history.HistoryTypeRepository;
import com.allcitizens.domain.history.RequestHistory;
import com.allcitizens.domain.history.RequestHistoryRepository;
import com.allcitizens.domain.request.ServiceRequestRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public class RequestHistoryApplicationService
        implements ListRequestHistoryUseCase, AppendRequestHistoryUseCase {

    private final RequestHistoryRepository historyRepository;
    private final ServiceRequestRepository serviceRequestRepository;
    private final HistoryTypeRepository historyTypeRepository;

    public RequestHistoryApplicationService(
            RequestHistoryRepository historyRepository,
            ServiceRequestRepository serviceRequestRepository,
            HistoryTypeRepository historyTypeRepository) {
        this.historyRepository = historyRepository;
        this.serviceRequestRepository = serviceRequestRepository;
        this.historyTypeRepository = historyTypeRepository;
    }

    @Override
    @Transactional
    public List<RequestHistoryResult> execute(UUID requestId) {
        if (serviceRequestRepository.findById(requestId).isEmpty()) {
            throw new EntityNotFoundException("ServiceRequest", requestId);
        }
        return historyRepository.findAllByRequestIdOrderByCreatedAtDesc(requestId).stream()
                .map(RequestHistoryResult::fromDomain)
                .toList();
    }

    @Override
    @Transactional
    public RequestHistoryResult execute(AppendRequestHistoryCommand command) {
        if (serviceRequestRepository.findById(command.requestId()).isEmpty()) {
            throw new EntityNotFoundException("ServiceRequest", command.requestId());
        }
        if (command.historyTypeId() != null
                && historyTypeRepository.findById(command.historyTypeId()).isEmpty()) {
            throw new EntityNotFoundException("HistoryType", command.historyTypeId());
        }
        var entry = RequestHistory.create(
                command.requestId(),
                command.historyTypeId(),
                command.userId(),
                command.description(),
                command.previousData(),
                command.newData()
        );
        var saved = historyRepository.save(entry);
        return RequestHistoryResult.fromDomain(saved);
    }
}
