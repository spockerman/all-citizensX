package com.allcitizens.application.request.service;

import com.allcitizens.application.request.command.CreateServiceRequestCommand;
import com.allcitizens.application.request.query.ListServiceRequestsQuery;
import com.allcitizens.domain.common.PageResult;
import com.allcitizens.domain.exception.EntityNotFoundException;
import com.allcitizens.domain.request.Channel;
import com.allcitizens.domain.request.Priority;
import com.allcitizens.domain.request.RequestStatus;
import com.allcitizens.domain.request.ServiceRequest;
import com.allcitizens.domain.request.ServiceRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceRequestApplicationServiceTest {

    @Mock
    private ServiceRequestRepository repository;

    private ServiceRequestApplicationService service;

    @BeforeEach
    void setUp() {
        service = new ServiceRequestApplicationService(repository);
    }

    @Test
    void shouldCreateServiceRequest() {
        var command = new CreateServiceRequestCommand(
                UUID.randomUUID(), "PROTO-001", UUID.randomUUID(),
                null, null, Channel.PHONE, Priority.NORMAL,
                "Test description", false, false, Map.of(), null, null
        );

        when(repository.save(any(ServiceRequest.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        var result = service.execute(command);

        assertThat(result).isNotNull();
        assertThat(result.protocol()).isEqualTo("PROTO-001");
        assertThat(result.status()).isEqualTo(RequestStatus.OPEN);
        verify(repository).save(any(ServiceRequest.class));
    }

    @Test
    void shouldThrowWhenServiceRequestNotFound() {
        var id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("ServiceRequest");
    }

    @Test
    void shouldGetServiceRequest() {
        var domain = ServiceRequest.create(UUID.randomUUID(), "PROTO-002",
                UUID.randomUUID(), Channel.WEB, Priority.HIGH, "Found");
        when(repository.findById(domain.getId())).thenReturn(Optional.of(domain));

        var result = service.get(domain.getId());

        assertThat(result.protocol()).isEqualTo("PROTO-002");
        assertThat(result.priority()).isEqualTo(Priority.HIGH);
    }

    @Test
    void shouldCloseServiceRequest() {
        var domain = ServiceRequest.create(UUID.randomUUID(), "PROTO-003",
                UUID.randomUUID(), Channel.EMAIL, Priority.LOW, "Close me");
        when(repository.findById(domain.getId())).thenReturn(Optional.of(domain));
        when(repository.save(any(ServiceRequest.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        var result = service.close(domain.getId());

        assertThat(result.status()).isEqualTo(RequestStatus.CLOSED);
        assertThat(result.closedAt()).isNotNull();
    }

    @Test
    void shouldListServiceRequestsPaginated() {
        var tenantId = UUID.randomUUID();
        var domain = ServiceRequest.create(tenantId, "PROTO-PAGE",
                UUID.randomUUID(), Channel.PHONE, Priority.NORMAL, "Desc");
        when(repository.findAllByTenantIdPaged(tenantId, 0, 10))
                .thenReturn(new PageResult<>(List.of(domain), 1, 0, 10));

        var page = service.execute(new ListServiceRequestsQuery(tenantId, 0, 10, null));

        assertThat(page.content()).hasSize(1);
        assertThat(page.totalElements()).isEqualTo(1);
        assertThat(page.content().get(0).protocol()).isEqualTo("PROTO-PAGE");
    }

    @Test
    void shouldCancelServiceRequest() {
        var domain = ServiceRequest.create(UUID.randomUUID(), "PROTO-004",
                UUID.randomUUID(), Channel.MOBILE_APP, Priority.URGENT, "Cancel me");
        when(repository.findById(domain.getId())).thenReturn(Optional.of(domain));
        when(repository.save(any(ServiceRequest.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        var result = service.cancel(domain.getId());

        assertThat(result.status()).isEqualTo(RequestStatus.CANCELLED);
        assertThat(result.closedAt()).isNotNull();
    }
}
