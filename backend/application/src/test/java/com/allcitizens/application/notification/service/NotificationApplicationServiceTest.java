package com.allcitizens.application.notification.service;

import com.allcitizens.application.notification.command.CreateNotificationCommand;
import com.allcitizens.application.notification.command.UpdateNotificationStatusCommand;
import com.allcitizens.application.notification.query.ListNotificationsQuery;
import com.allcitizens.domain.exception.BusinessRuleException;
import com.allcitizens.domain.notification.Notification;
import com.allcitizens.domain.notification.NotificationChannel;
import com.allcitizens.domain.notification.NotificationRepository;
import com.allcitizens.domain.common.PageResult;
import com.allcitizens.domain.request.ServiceRequest;
import com.allcitizens.domain.request.ServiceRequestRepository;
import com.allcitizens.domain.tenant.Tenant;
import com.allcitizens.domain.tenant.TenantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
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
class NotificationApplicationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @InjectMocks
    private NotificationApplicationService service;

    @Test
    void createSavesWhenTenantAndRequestOk() {
        var tenantId = UUID.randomUUID();
        var requestId = UUID.randomUUID();
        when(tenantRepository.findById(tenantId))
                .thenReturn(Optional.of(Tenant.reconstitute(
                        tenantId, "T", "code", true, Map.of(), Instant.now(), Instant.now())));
        var req = org.mockito.Mockito.mock(ServiceRequest.class);
        when(req.getTenantId()).thenReturn(tenantId);
        when(serviceRequestRepository.findById(requestId)).thenReturn(Optional.of(req));
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        var result = service.execute(
                new CreateNotificationCommand(tenantId, requestId, null, "EMAIL", "t", "msg", null));

        assertThat(result.channel()).isEqualTo("EMAIL");
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void createRejectsRequestFromOtherTenant() {
        var tenantId = UUID.randomUUID();
        var requestId = UUID.randomUUID();
        when(tenantRepository.findById(tenantId))
                .thenReturn(Optional.of(Tenant.reconstitute(
                        tenantId, "T", "c1", true, Map.of(), Instant.now(), Instant.now())));
        var req = org.mockito.Mockito.mock(ServiceRequest.class);
        when(req.getTenantId()).thenReturn(UUID.randomUUID());
        when(serviceRequestRepository.findById(requestId)).thenReturn(Optional.of(req));

        assertThatThrownBy(() -> service.execute(
                        new CreateNotificationCommand(tenantId, requestId, null, "EMAIL", null, "m", null)))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("tenant");
    }

    @Test
    void updateStatusSent() {
        var id = UUID.randomUUID();
        var n = Notification.create(
                UUID.randomUUID(), null, null, NotificationChannel.PUSH, null, "m", null);
        when(notificationRepository.findById(id)).thenReturn(Optional.of(n));
        when(notificationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var r = service.execute(id, new UpdateNotificationStatusCommand("SENT", Instant.now(), null));
        assertThat(r.status()).isEqualTo("SENT");
    }

    @Test
    void listByTenant() {
        var tenantId = UUID.randomUUID();
        var n = Notification.create(tenantId, null, null, NotificationChannel.EMAIL, null, "x", null);
        when(notificationRepository.findByTenantId(tenantId, 0, 20))
                .thenReturn(new PageResult<>(List.of(n), 1, 0, 20));

        var page = service.execute(new ListNotificationsQuery(tenantId, null, 0, 20));
        assertThat(page.content()).hasSize(1);
    }
}
