package com.allcitizens.application.notification.service;

import com.allcitizens.application.notification.command.CreateNotificationRuleCommand;
import com.allcitizens.domain.catalog.CatalogService;
import com.allcitizens.domain.catalog.CatalogServiceRepository;
import com.allcitizens.domain.notification.NotificationRule;
import com.allcitizens.domain.notification.NotificationRuleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationRuleApplicationServiceTest {

    @Mock
    private NotificationRuleRepository ruleRepository;

    @Mock
    private CatalogServiceRepository catalogServiceRepository;

    @InjectMocks
    private NotificationRuleApplicationService service;

    @Test
    void createRequiresCatalogService() {
        var serviceId = UUID.randomUUID();
        when(catalogServiceRepository.findById(serviceId)).thenReturn(Optional.of(org.mockito.Mockito.mock(CatalogService.class)));
        when(ruleRepository.save(any(NotificationRule.class))).thenAnswer(inv -> inv.getArgument(0));

        var r = service.execute(
                new CreateNotificationRuleCommand(serviceId, "EVT", "EMAIL", "Hello"));

        assertThat(r.event()).isEqualTo("EVT");
        verify(ruleRepository).save(any());
    }

    @Test
    void listForService() {
        var sid = UUID.randomUUID();
        when(catalogServiceRepository.findById(sid)).thenReturn(Optional.of(org.mockito.Mockito.mock(CatalogService.class)));
        var rule = NotificationRule.create(sid, "E", com.allcitizens.domain.notification.NotificationChannel.SMS, "T");
        when(ruleRepository.findByServiceId(sid)).thenReturn(List.of(rule));

        var list = service.listForService(sid);
        assertThat(list).hasSize(1);
    }
}
