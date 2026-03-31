package com.allcitizens.domain.subject;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubjectTest {

    private final UUID tenantId = UUID.randomUUID();

    @Test
    void shouldCreateValidSubject() {
        var subject = Subject.create(tenantId, "Taxes");

        assertThat(subject.getId()).isNotNull();
        assertThat(subject.getTenantId()).isEqualTo(tenantId);
        assertThat(subject.getName()).isEqualTo("Taxes");
        assertThat(subject.getDepartmentId()).isNull();
        assertThat(subject.isActive()).isTrue();
        assertThat(subject.isVisibleWeb()).isTrue();
        assertThat(subject.isVisibleApp()).isTrue();
        assertThat(subject.getCreatedAt()).isNotNull();
        assertThat(subject.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        assertThatThrownBy(() -> Subject.create(tenantId, ""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name must not be blank");
    }

    @Test
    void shouldFailWhenNameIsNull() {
        assertThatThrownBy(() -> Subject.create(tenantId, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name must not be blank");
    }

    @Test
    void shouldFailWhenTenantIdIsNull() {
        assertThatThrownBy(() -> Subject.create(null, "Taxes"))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("tenantId must not be null");
    }

    @Test
    void shouldAssignDepartment() {
        var subject = Subject.create(tenantId, "Taxes");
        var departmentId = UUID.randomUUID();

        subject.assignDepartment(departmentId);

        assertThat(subject.getDepartmentId()).isEqualTo(departmentId);
    }

    @Test
    void shouldFailAssignDepartmentWithNull() {
        var subject = Subject.create(tenantId, "Taxes");

        assertThatThrownBy(() -> subject.assignDepartment(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("departmentId must not be null");
    }

    @Test
    void shouldRemoveDepartment() {
        var subject = Subject.create(tenantId, "Taxes");
        subject.assignDepartment(UUID.randomUUID());

        subject.removeDepartment();

        assertThat(subject.getDepartmentId()).isNull();
    }

    @Test
    void shouldSetVisibility() {
        var subject = Subject.create(tenantId, "Taxes");

        subject.setVisibility(false, true);

        assertThat(subject.isVisibleWeb()).isFalse();
        assertThat(subject.isVisibleApp()).isTrue();
    }

    @Test
    void shouldActivate() {
        var subject = Subject.create(tenantId, "Taxes");
        subject.deactivate();

        subject.activate();

        assertThat(subject.isActive()).isTrue();
    }

    @Test
    void shouldDeactivate() {
        var subject = Subject.create(tenantId, "Taxes");

        subject.deactivate();

        assertThat(subject.isActive()).isFalse();
    }

    @Test
    void shouldUpdateSubject() {
        var subject = Subject.create(tenantId, "Old Name");
        var previousUpdatedAt = subject.getUpdatedAt();

        subject.update("New Name");

        assertThat(subject.getName()).isEqualTo("New Name");
        assertThat(subject.getUpdatedAt()).isAfterOrEqualTo(previousUpdatedAt);
    }

    @Test
    void shouldFailUpdateWithBlankName() {
        var subject = Subject.create(tenantId, "Taxes");

        assertThatThrownBy(() -> subject.update(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name must not be blank");
    }

    @Test
    void shouldReconstitute() {
        var id = UUID.randomUUID();
        var deptId = UUID.randomUUID();
        var created = Instant.parse("2024-01-01T00:00:00Z");
        var updated = Instant.parse("2024-01-02T00:00:00Z");

        var subject = Subject.reconstitute(id, tenantId, deptId, "Stored", false, false, false, created, updated);

        assertThat(subject.getId()).isEqualTo(id);
        assertThat(subject.getTenantId()).isEqualTo(tenantId);
        assertThat(subject.getDepartmentId()).isEqualTo(deptId);
        assertThat(subject.getName()).isEqualTo("Stored");
        assertThat(subject.isActive()).isFalse();
        assertThat(subject.isVisibleWeb()).isFalse();
        assertThat(subject.isVisibleApp()).isFalse();
        assertThat(subject.getCreatedAt()).isEqualTo(created);
        assertThat(subject.getUpdatedAt()).isEqualTo(updated);
    }
}
