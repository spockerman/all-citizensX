package com.allcitizens.domain.notification;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationRuleTest {

    @Test
    void createAndUpdate() {
        var rule =
                NotificationRule.create(UUID.randomUUID(), "REQUEST_CREATED", NotificationChannel.PUSH, "Hello {{name}}");
        assertThat(rule.isActive()).isTrue();
        rule.update("OTHER", null, "T2", null);
        assertThat(rule.getEvent()).isEqualTo("OTHER");
        assertThat(rule.getTemplate()).isEqualTo("T2");
    }

    @Test
    void eventTooLong() {
        assertThatThrownBy(() -> NotificationRule.create(
                        UUID.randomUUID(),
                        "x".repeat(51),
                        NotificationChannel.EMAIL,
                        "t"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
