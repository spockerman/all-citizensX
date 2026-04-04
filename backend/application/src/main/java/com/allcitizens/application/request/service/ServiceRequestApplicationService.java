package com.allcitizens.application.request.service;

import com.allcitizens.application.request.command.CreateServiceRequestCommand;
import com.allcitizens.application.request.command.UpdateServiceRequestCommand;
import com.allcitizens.application.request.result.ServiceRequestResult;
import com.allcitizens.application.request.usecase.CreateServiceRequestUseCase;
import com.allcitizens.application.request.usecase.ListServiceRequestsUseCase;
import com.allcitizens.application.request.usecase.UpdateServiceRequestUseCase;
import com.allcitizens.domain.exception.EntityNotFoundException;
import com.allcitizens.domain.request.ServiceRequest;
import com.allcitizens.domain.request.ServiceRequestRepository;
import jakarta.transaction.Transactional;

import com.allcitizens.application.request.query.ListServiceRequestsQuery;
import com.allcitizens.domain.common.PageResult;

import java.util.UUID;

public class ServiceRequestApplicationService
        implements CreateServiceRequestUseCase, UpdateServiceRequestUseCase, ListServiceRequestsUseCase {

    private final ServiceRequestRepository repository;

    public ServiceRequestApplicationService(ServiceRequestRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public ServiceRequestResult execute(CreateServiceRequestCommand command) {
        var request = ServiceRequest.create(
                command.tenantId(),
                command.protocol(),
                command.serviceId(),
                command.channel(),
                command.priority(),
                command.description()
        );

        if (command.personId() != null) {
            request.assignPerson(command.personId());
        }
        if (command.addressId() != null) {
            request.assignAddress(command.addressId());
        }
        if (command.dynamicFields() != null && !command.dynamicFields().isEmpty()) {
            request.setDynamicFields(command.dynamicFields());
        }
        if (command.latitude() != null && command.longitude() != null) {
            request.setCoordinates(command.latitude(), command.longitude());
        }
        request.setConfidential(command.confidential());
        request.setAnonymous(command.anonymous());

        var saved = repository.save(request);
        return ServiceRequestResult.fromDomain(saved);
    }

    public ServiceRequestResult get(UUID id) {
        var request = findOrThrow(id);
        return ServiceRequestResult.fromDomain(request);
    }

    @Override
    @Transactional
    public ServiceRequestResult execute(UUID id, UpdateServiceRequestCommand command) {
        var request = findOrThrow(id);

        if (command.description() != null) {
            request.updateDescription(command.description());
        }
        if (command.personId() != null) {
            request.assignPerson(command.personId());
        }
        if (command.addressId() != null) {
            request.assignAddress(command.addressId());
        }

        var saved = repository.save(request);
        return ServiceRequestResult.fromDomain(saved);
    }

    @Override
    @Transactional
    public PageResult<ServiceRequestResult> execute(ListServiceRequestsQuery query) {
        var page = (query.search() == null || query.search().isBlank())
                ? repository.findAllByTenantIdPaged(query.tenantId(), query.page(), query.size())
                : repository.searchByTenantIdPaged(
                        query.tenantId(), query.search().trim(), query.page(), query.size());
        var content = page.content().stream()
                .map(ServiceRequestResult::fromDomain)
                .toList();
        return new PageResult<>(content, page.totalElements(), page.page(), page.size());
    }

    @Transactional
    public ServiceRequestResult close(UUID id) {
        var request = findOrThrow(id);
        request.close();
        var saved = repository.save(request);
        return ServiceRequestResult.fromDomain(saved);
    }

    @Transactional
    public ServiceRequestResult cancel(UUID id) {
        var request = findOrThrow(id);
        request.cancel();
        var saved = repository.save(request);
        return ServiceRequestResult.fromDomain(saved);
    }

    private ServiceRequest findOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ServiceRequest", id));
    }
}
