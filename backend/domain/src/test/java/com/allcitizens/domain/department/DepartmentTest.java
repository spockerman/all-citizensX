package com.allcitizens.domain.department;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DepartmentTest {

    private final UUID tenantId = UUID.randomUUID();

    @Test
    void shouldCreateValidDepartment() {
        var department = Department.create(tenantId, "Engineering", "ENG", "eng@test.com", true, false, 1);

        assertThat(department.getId()).isNotNull();
        assertThat(department.getTenantId()).isEqualTo(tenantId);
        assertThat(department.getName()).isEqualTo("Engineering");
        assertThat(department.getAbbreviation()).isEqualTo("ENG");
        assertThat(department.getEmail()).isEqualTo("eng@test.com");
        assertThat(department.isActive()).isTrue();
        assertThat(department.isCanRespond()).isTrue();
        assertThat(department.isRoot()).isFalse();
        assertThat(department.getDisplayOrder()).isEqualTo(1);
        assertThat(department.getParentId()).isNull();
        assertThat(department.getCreatedAt()).isNotNull();
        assertThat(department.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        assertThatThrownBy(() -> Department.create(tenantId, "", null, null, false, false, 0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name must not be blank");
    }

    @Test
    void shouldFailWhenNameIsNull() {
        assertThatThrownBy(() -> Department.create(tenantId, null, null, null, false, false, 0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name must not be blank");
    }

    @Test
    void shouldFailWhenTenantIdIsNull() {
        assertThatThrownBy(() -> Department.create(null, "Engineering", null, null, false, false, 0))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("tenantId must not be null");
    }

    @Test
    void shouldAssignParent() {
        var department = Department.create(tenantId, "Sub Department", null, null, false, false, 0);
        var parentId = UUID.randomUUID();

        department.assignParent(parentId);

        assertThat(department.getParentId()).isEqualTo(parentId);
    }

    @Test
    void shouldFailAssignParentWithNull() {
        var department = Department.create(tenantId, "Sub Department", null, null, false, false, 0);

        assertThatThrownBy(() -> department.assignParent(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("parentId must not be null");
    }

    @Test
    void shouldRemoveParent() {
        var department = Department.create(tenantId, "Sub Department", null, null, false, false, 0);
        department.assignParent(UUID.randomUUID());

        department.removeParent();

        assertThat(department.getParentId()).isNull();
    }

    @Test
    void shouldActivate() {
        var department = Department.create(tenantId, "Department", null, null, false, false, 0);
        department.deactivate();

        department.activate();

        assertThat(department.isActive()).isTrue();
    }

    @Test
    void shouldDeactivate() {
        var department = Department.create(tenantId, "Department", null, null, false, false, 0);

        department.deactivate();

        assertThat(department.isActive()).isFalse();
    }

    @Test
    void shouldUpdateDepartment() {
        var department = Department.create(tenantId, "Old Name", "OLD", "old@test.com", false, false, 0);
        var previousUpdatedAt = department.getUpdatedAt();

        department.update("New Name", "NEW", "new@test.com", true, 5);

        assertThat(department.getName()).isEqualTo("New Name");
        assertThat(department.getAbbreviation()).isEqualTo("NEW");
        assertThat(department.getEmail()).isEqualTo("new@test.com");
        assertThat(department.isCanRespond()).isTrue();
        assertThat(department.getDisplayOrder()).isEqualTo(5);
        assertThat(department.getUpdatedAt()).isAfterOrEqualTo(previousUpdatedAt);
    }

    @Test
    void shouldFailUpdateWithBlankName() {
        var department = Department.create(tenantId, "Department", null, null, false, false, 0);

        assertThatThrownBy(() -> department.update("", null, null, false, 0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name must not be blank");
    }
}
