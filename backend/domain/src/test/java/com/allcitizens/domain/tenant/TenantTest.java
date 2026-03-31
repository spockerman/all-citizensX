package com.allcitizens.domain.tenant;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TenantTest {

    @Test
    void shouldCreateValidTenant() {
        var tenant = Tenant.create("City of Springfield", "SPR");

        assertThat(tenant.getId()).isNotNull();
        assertThat(tenant.getName()).isEqualTo("City of Springfield");
        assertThat(tenant.getCode()).isEqualTo("SPR");
        assertThat(tenant.isActive()).isTrue();
        assertThat(tenant.getConfig()).isEmpty();
        assertThat(tenant.getCreatedAt()).isNotNull();
        assertThat(tenant.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        assertThatThrownBy(() -> Tenant.create("", "SPR"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name must not be blank");
    }

    @Test
    void shouldFailWhenNameIsNull() {
        assertThatThrownBy(() -> Tenant.create(null, "SPR"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name must not be blank");
    }

    @Test
    void shouldFailWhenCodeIsBlank() {
        assertThatThrownBy(() -> Tenant.create("City of Springfield", ""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("code must not be blank");
    }

    @Test
    void shouldFailWhenCodeIsNull() {
        assertThatThrownBy(() -> Tenant.create("City of Springfield", null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("code must not be blank");
    }

    @Test
    void shouldFailWhenCodeExceeds20Characters() {
        var longCode = "A".repeat(21);
        assertThatThrownBy(() -> Tenant.create("City of Springfield", longCode))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("code must not exceed 20 characters");
    }

    @Test
    void shouldAcceptCodeWith20Characters() {
        var code = "A".repeat(20);
        var tenant = Tenant.create("City of Springfield", code);
        assertThat(tenant.getCode()).hasSize(20);
    }

    @Test
    void shouldUpdateName() {
        var tenant = Tenant.create("Old Name", "SPR");
        var previousUpdatedAt = tenant.getUpdatedAt();

        tenant.update("New Name");

        assertThat(tenant.getName()).isEqualTo("New Name");
        assertThat(tenant.getUpdatedAt()).isAfterOrEqualTo(previousUpdatedAt);
    }

    @Test
    void shouldFailUpdateWithBlankName() {
        var tenant = Tenant.create("City of Springfield", "SPR");

        assertThatThrownBy(() -> tenant.update(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name must not be blank");
    }

    @Test
    void shouldActivate() {
        var tenant = Tenant.create("City of Springfield", "SPR");
        tenant.deactivate();

        tenant.activate();

        assertThat(tenant.isActive()).isTrue();
    }

    @Test
    void shouldDeactivate() {
        var tenant = Tenant.create("City of Springfield", "SPR");

        tenant.deactivate();

        assertThat(tenant.isActive()).isFalse();
    }

    @Test
    void shouldReconstituteTenant() {
        var id = UUID.randomUUID();
        var config = Map.<String, Object>of("theme", "dark");
        var now = Instant.now();

        var tenant = Tenant.reconstitute(id, "City", "CTY", true, config, now, now);

        assertThat(tenant.getId()).isEqualTo(id);
        assertThat(tenant.getName()).isEqualTo("City");
        assertThat(tenant.getCode()).isEqualTo("CTY");
        assertThat(tenant.isActive()).isTrue();
        assertThat(tenant.getConfig()).containsEntry("theme", "dark");
        assertThat(tenant.getCreatedAt()).isEqualTo(now);
        assertThat(tenant.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void shouldUpdateConfig() {
        var tenant = Tenant.create("City of Springfield", "SPR");
        var newConfig = Map.<String, Object>of("language", "en");

        tenant.updateConfig(newConfig);

        assertThat(tenant.getConfig()).containsEntry("language", "en");
    }
}
