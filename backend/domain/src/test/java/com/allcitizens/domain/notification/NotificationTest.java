package com.allcitizens.domain.notification;

import com.allcitizens.domain.exception.BusinessRuleException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationTest {

    @Test
    void createPending() {
        var tenant = UUID.randomUUID();
        var n = Notification.create(
                tenant, null, null, NotificationChannel.EMAIL, "Hi", "Body", Map.of("k", "v"));
        assertThat(n.getStatus()).isEqualTo(NotificationStatus.PENDING);
        assertThat(n.getTenantId()).isEqualTo(tenant);
    }

    @Test
    void markSentThenRead() {
        var n = Notification.create(
                UUID.randomUUID(), null, null, NotificationChannel.IN_APP, null, "m", null);
        n.markSent(Instant.now());
        assertThat(n.getStatus()).isEqualTo(NotificationStatus.SENT);
        n.markRead(Instant.now());
        assertThat(n.getStatus()).isEqualTo(NotificationStatus.READ);
    }

    @Test
    void cannotMarkSentTwice() {
        var n = Notification.create(
                UUID.randomUUID(), null, null, NotificationChannel.SMS, null, "x", null);
        n.markSent(Instant.now());
        assertThatThrownBy(() -> n.markSent(Instant.now())).isInstanceOf(BusinessRuleException.class);
    }
}
