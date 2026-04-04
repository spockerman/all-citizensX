package com.allcitizens.application.forwarding.service;

import com.allcitizens.application.forwarding.command.CreateForwardingReasonCommand;
import com.allcitizens.application.forwarding.result.ForwardingReasonResult;
import com.allcitizens.application.forwarding.usecase.CreateForwardingReasonUseCase;
import com.allcitizens.application.forwarding.usecase.ListForwardingReasonsUseCase;
import com.allcitizens.domain.forwarding.ForwardingReason;
import com.allcitizens.domain.forwarding.ForwardingReasonRepository;
import jakarta.transaction.Transactional;

import java.util.List;

public class ForwardingReasonApplicationService
        implements CreateForwardingReasonUseCase, ListForwardingReasonsUseCase {

    private final ForwardingReasonRepository repository;

    public ForwardingReasonApplicationService(ForwardingReasonRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public ForwardingReasonResult execute(CreateForwardingReasonCommand command) {
        var reason = ForwardingReason.create(command.name(), command.type());
        var saved = repository.save(reason);
        return ForwardingReasonResult.fromDomain(saved);
    }

    @Override
    @Transactional
    public List<ForwardingReasonResult> execute() {
        return repository.findAll().stream()
                .map(ForwardingReasonResult::fromDomain)
                .toList();
    }
}
