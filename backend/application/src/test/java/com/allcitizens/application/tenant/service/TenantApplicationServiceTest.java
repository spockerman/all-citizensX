package com.allcitizens.application.tenant.service;

import com.allcitizens.application.tenant.command.CreateTenantCommand;
import com.allcitizens.application.tenant.query.ListTenantsQuery;
import com.allcitizens.application.tenant.command.UpdateTenantCommand;
import com.allcitizens.domain.common.PageResult;
import com.allcitizens.domain.exception.BusinessRuleException;
import com.allcitizens.domain.exception.EntityNotFoundException;
import com.allcitizens.domain.tenant.Tenant;
import com.allcitizens.domain.tenant.TenantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TenantApplicationServiceTest {

    @Mock
    private TenantRepository tenantRepository;

    private TenantApplicationService service;

    @BeforeEach
    void setUp() {
        service = new TenantApplicationService(tenantRepository);
    }

    @Test
    void shouldCreateTenant() {
        var command = new CreateTenantCommand("City of Springfield", "SPR");

        when(tenantRepository.existsByCode("SPR")).thenReturn(false);
        when(tenantRepository.save(any(Tenant.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.execute(command);

        assertThat(result.name()).isEqualTo("City of Springfield");
        assertThat(result.code()).isEqualTo("SPR");
        assertThat(result.active()).isTrue();
        verify(tenantRepository).save(any(Tenant.class));
    }

    @Test
    void shouldThrowWhenCreatingTenantWithDuplicateCode() {
        var command = new CreateTenantCommand("City of Springfield", "SPR");

        when(tenantRepository.existsByCode("SPR")).thenReturn(true);

        assertThatThrownBy(() -> service.execute(command))
            .isInstanceOf(BusinessRuleException.class)
            .hasMessageContaining("already exists");
        verify(tenantRepository, never()).save(any());
    }

    @Test
    void shouldGetTenantById() {
        var id = UUID.randomUUID();
        var tenant = Tenant.create("City of Springfield", "SPR");

        when(tenantRepository.findById(id)).thenReturn(Optional.of(tenant));

        var result = service.execute(id);

        assertThat(result.name()).isEqualTo("City of Springfield");
        assertThat(result.code()).isEqualTo("SPR");
    }

    @Test
    void shouldThrowWhenTenantNotFound() {
        var id = UUID.randomUUID();
        when(tenantRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.execute(id))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldUpdateTenant() {
        var id = UUID.randomUUID();
        var tenant = Tenant.create("Old Name", "SPR");
        var command = new UpdateTenantCommand("New Name", null);

        when(tenantRepository.findById(id)).thenReturn(Optional.of(tenant));
        when(tenantRepository.save(any(Tenant.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.execute(id, command);

        assertThat(result.name()).isEqualTo("New Name");
        assertThat(result.active()).isTrue();
    }

    @Test
    void shouldDeactivateTenant() {
        var id = UUID.randomUUID();
        var tenant = Tenant.create("City", "SPR");
        var command = new UpdateTenantCommand(null, false);

        when(tenantRepository.findById(id)).thenReturn(Optional.of(tenant));
        when(tenantRepository.save(any(Tenant.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.execute(id, command);

        assertThat(result.active()).isFalse();
    }

    @Test
    void shouldListAllTenants() {
        var tenant1 = Tenant.create("City A", "CA");
        var tenant2 = Tenant.create("City B", "CB");

        when(tenantRepository.findAllPaged(0, 20))
            .thenReturn(new PageResult<>(List.of(tenant1, tenant2), 2, 0, 20));

        var results = service.execute(new ListTenantsQuery(0, 20, null));

        assertThat(results.content()).hasSize(2);
        assertThat(results.content().get(0).name()).isEqualTo("City A");
        assertThat(results.content().get(1).name()).isEqualTo("City B");
    }

    @Test
    void shouldDeleteTenant() {
        var id = UUID.randomUUID();
        var tenant = Tenant.create("City", "SPR");

        when(tenantRepository.findById(id)).thenReturn(Optional.of(tenant));

        service.delete(id);

        verify(tenantRepository).deleteById(id);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentTenant() {
        var id = UUID.randomUUID();
        when(tenantRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(id))
            .isInstanceOf(EntityNotFoundException.class);
        verify(tenantRepository, never()).deleteById(any());
    }
}
