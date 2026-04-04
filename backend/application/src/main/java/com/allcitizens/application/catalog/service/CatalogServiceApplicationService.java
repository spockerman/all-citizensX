package com.allcitizens.application.catalog.service;

import com.allcitizens.application.catalog.command.CreateCatalogServiceCommand;
import com.allcitizens.application.catalog.command.UpdateCatalogServiceCommand;
import com.allcitizens.application.catalog.query.ListCatalogServicesQuery;
import com.allcitizens.application.catalog.result.CatalogServiceResult;
import com.allcitizens.application.catalog.usecase.ListCatalogServicesUseCase;
import com.allcitizens.domain.catalog.CatalogService;
import com.allcitizens.domain.common.PageResult;
import com.allcitizens.domain.catalog.CatalogServiceRepository;
import com.allcitizens.domain.exception.EntityNotFoundException;
import com.allcitizens.domain.request.Priority;
import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.UUID;

public class CatalogServiceApplicationService implements ListCatalogServicesUseCase {

    private final CatalogServiceRepository catalogServiceRepository;

    public CatalogServiceApplicationService(CatalogServiceRepository catalogServiceRepository) {
        this.catalogServiceRepository = catalogServiceRepository;
    }

    @Transactional
    public CatalogServiceResult create(CreateCatalogServiceCommand command) {
        Priority priority = Priority.valueOf(command.defaultPriority().trim().toUpperCase());
        var catalogService = CatalogService.create(
            command.tenantId(),
            command.subjectId(),
            command.subdivisionId(),
            command.displayName(),
            command.slaDays()
        );
        catalogService.update(
            command.displayName(),
            command.description(),
            command.slaDays(),
            priority,
            command.allowsAnonymous(),
            command.allowsMultiForward(),
            new HashMap<>()
        );
        catalogService.setVisibility(command.visibleWeb(), command.visibleApp());
        if (command.departmentId() != null) {
            catalogService.assignDepartment(command.departmentId());
        }
        catalogService = catalogServiceRepository.save(catalogService);
        return CatalogServiceResult.fromDomain(catalogService);
    }

    public CatalogServiceResult getById(UUID id) {
        var catalogService = catalogServiceRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CatalogService", id));
        return CatalogServiceResult.fromDomain(catalogService);
    }

    @Transactional
    public CatalogServiceResult update(UUID id, UpdateCatalogServiceCommand command) {
        var catalogService = catalogServiceRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CatalogService", id));

        String displayName = command.displayName() != null ? command.displayName() : catalogService.getDisplayName();
        String description = command.description() != null ? command.description() : catalogService.getDescription();
        int slaDays = command.slaDays() != null ? command.slaDays() : catalogService.getSlaDays();
        Priority defaultPriority = command.defaultPriority() != null
            ? Priority.valueOf(command.defaultPriority().trim().toUpperCase())
            : catalogService.getDefaultPriority();
        boolean allowsAnonymous = command.allowsAnonymous() != null
            ? command.allowsAnonymous()
            : catalogService.isAllowsAnonymous();
        boolean allowsMultiForward = command.allowsMultiForward() != null
            ? command.allowsMultiForward()
            : catalogService.isAllowsMultiForward();
        var dynamicFields = catalogService.getDynamicFields();

        catalogService.update(
            displayName,
            description,
            slaDays,
            defaultPriority,
            allowsAnonymous,
            allowsMultiForward,
            dynamicFields
        );

        if (command.visibleWeb() != null || command.visibleApp() != null) {
            catalogService.setVisibility(
                command.visibleWeb() != null ? command.visibleWeb() : catalogService.isVisibleWeb(),
                command.visibleApp() != null ? command.visibleApp() : catalogService.isVisibleApp()
            );
        }
        if (command.active() != null) {
            if (command.active()) {
                catalogService.activate();
            } else {
                catalogService.deactivate();
            }
        }
        if (command.departmentId() != null) {
            catalogService.assignDepartment(command.departmentId());
        }

        catalogService = catalogServiceRepository.save(catalogService);
        return CatalogServiceResult.fromDomain(catalogService);
    }

    @Override
    public PageResult<CatalogServiceResult> execute(ListCatalogServicesQuery query) {
        var page = (query.search() == null || query.search().isBlank())
                ? catalogServiceRepository.findAllByTenantIdPaged(
                        query.tenantId(), query.page(), query.size())
                : catalogServiceRepository.searchByTenantIdPaged(
                        query.tenantId(), query.search().trim(), query.page(), query.size());
        var content = page.content().stream().map(CatalogServiceResult::fromDomain).toList();
        return new PageResult<>(content, page.totalElements(), page.page(), page.size());
    }

    @Transactional
    public void delete(UUID id) {
        if (!catalogServiceRepository.existsById(id)) {
            throw new EntityNotFoundException("CatalogService", id);
        }
        catalogServiceRepository.deleteById(id);
    }
}
