package com.allcitizens.application.tenant.service;

import com.allcitizens.application.tenant.command.CreateTenantCommand;
import com.allcitizens.application.tenant.command.UpdateTenantCommand;
import com.allcitizens.application.tenant.result.TenantResult;
import com.allcitizens.application.tenant.usecase.CreateTenantUseCase;
import com.allcitizens.application.tenant.usecase.GetTenantUseCase;
import com.allcitizens.application.tenant.usecase.ListTenantsUseCase;
import com.allcitizens.application.tenant.usecase.UpdateTenantUseCase;
import com.allcitizens.domain.exception.BusinessRuleException;
import com.allcitizens.domain.exception.EntityNotFoundException;
import com.allcitizens.domain.tenant.Tenant;
import com.allcitizens.domain.tenant.TenantRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public class TenantApplicationService implements CreateTenantUseCase,
        GetTenantUseCase, UpdateTenantUseCase, ListTenantsUseCase {

    private final TenantRepository tenantRepository;

    public TenantApplicationService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    @Transactional
    public TenantResult execute(CreateTenantCommand command) {
        if (tenantRepository.existsByCode(command.code())) {
            throw new BusinessRuleException("Tenant with code '" + command.code() + "' already exists");
        }

        var tenant = Tenant.create(command.name(), command.code());
        tenant = tenantRepository.save(tenant);
        return TenantResult.fromDomain(tenant);
    }

    @Override
    public TenantResult execute(UUID id) {
        var tenant = tenantRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Tenant", id));
        return TenantResult.fromDomain(tenant);
    }

    @Override
    @Transactional
    public TenantResult execute(UUID id, UpdateTenantCommand command) {
        var tenant = tenantRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Tenant", id));

        if (command.name() != null) {
            tenant.update(command.name());
        }

        if (command.active() != null) {
            if (command.active()) {
                tenant.activate();
            } else {
                tenant.deactivate();
            }
        }

        tenant = tenantRepository.save(tenant);
        return TenantResult.fromDomain(tenant);
    }

    @Override
    public List<TenantResult> execute() {
        return tenantRepository.findAll().stream()
            .map(TenantResult::fromDomain)
            .toList();
    }

    @Transactional
    public void delete(UUID id) {
        tenantRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Tenant", id));
        tenantRepository.deleteById(id);
    }
}
