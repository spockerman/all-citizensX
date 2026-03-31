package com.allcitizens.domain.catalog;

import com.allcitizens.domain.request.Priority;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CatalogServiceTest {

    private final UUID tenantId = UUID.randomUUID();
    private final UUID subjectId = UUID.randomUUID();
    private final UUID subdivisionId = UUID.randomUUID();

    @Test
    void shouldCreateValidCatalogService() {
        var service = CatalogService.create(tenantId, subjectId, subdivisionId, "Building permit", 30);

        assertThat(service.getId()).isNotNull();
        assertThat(service.getTenantId()).isEqualTo(tenantId);
        assertThat(service.getSubjectId()).isEqualTo(subjectId);
        assertThat(service.getSubdivisionId()).isEqualTo(subdivisionId);
        assertThat(service.getDepartmentId()).isNull();
        assertThat(service.getDisplayName()).isEqualTo("Building permit");
        assertThat(service.getDescription()).isNull();
        assertThat(service.getSlaDays()).isEqualTo(30);
        assertThat(service.getDefaultPriority()).isEqualTo(Priority.NORMAL);
        assertThat(service.isAllowsAnonymous()).isFalse();
        assertThat(service.isAllowsMultiForward()).isFalse();
        assertThat(service.isVisibleWeb()).isTrue();
        assertThat(service.isVisibleApp()).isTrue();
        assertThat(service.getDynamicFields()).isEmpty();
        assertThat(service.isActive()).isTrue();
        assertThat(service.getCreatedAt()).isNotNull();
        assertThat(service.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldAllowNullDisplayName() {
        var service = CatalogService.create(tenantId, subjectId, subdivisionId, null, 15);

        assertThat(service.getDisplayName()).isNull();
        assertThat(service.getSlaDays()).isEqualTo(15);
    }

    @Test
    void shouldFailWhenTenantIdIsNull() {
        assertThatThrownBy(() -> CatalogService.create(null, subjectId, subdivisionId, "X", 1))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("tenantId must not be null");
    }

    @Test
    void shouldFailWhenSubjectIdIsNull() {
        assertThatThrownBy(() -> CatalogService.create(tenantId, null, subdivisionId, "X", 1))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("subjectId must not be null");
    }

    @Test
    void shouldFailWhenSubdivisionIdIsNull() {
        assertThatThrownBy(() -> CatalogService.create(tenantId, subjectId, null, "X", 1))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("subdivisionId must not be null");
    }

    @Test
    void shouldFailWhenSlaDaysIsNegative() {
        assertThatThrownBy(() -> CatalogService.create(tenantId, subjectId, subdivisionId, "X", -1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("slaDays must not be negative");
    }

    @Test
    void shouldAssignDepartment() {
        var service = CatalogService.create(tenantId, subjectId, subdivisionId, "X", 10);
        var departmentId = UUID.randomUUID();

        service.assignDepartment(departmentId);

        assertThat(service.getDepartmentId()).isEqualTo(departmentId);
    }

    @Test
    void shouldFailAssignDepartmentWithNull() {
        var service = CatalogService.create(tenantId, subjectId, subdivisionId, "X", 10);

        assertThatThrownBy(() -> service.assignDepartment(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("departmentId must not be null");
    }

    @Test
    void shouldRemoveDepartment() {
        var service = CatalogService.create(tenantId, subjectId, subdivisionId, "X", 10);
        service.assignDepartment(UUID.randomUUID());

        service.removeDepartment();

        assertThat(service.getDepartmentId()).isNull();
    }

    @Test
    void shouldSetVisibility() {
        var service = CatalogService.create(tenantId, subjectId, subdivisionId, "X", 10);

        service.setVisibility(false, false);

        assertThat(service.isVisibleWeb()).isFalse();
        assertThat(service.isVisibleApp()).isFalse();
    }

    @Test
    void shouldActivate() {
        var service = CatalogService.create(tenantId, subjectId, subdivisionId, "X", 10);
        service.deactivate();

        service.activate();

        assertThat(service.isActive()).isTrue();
    }

    @Test
    void shouldDeactivate() {
        var service = CatalogService.create(tenantId, subjectId, subdivisionId, "X", 10);

        service.deactivate();

        assertThat(service.isActive()).isFalse();
    }

    @Test
    void shouldUpdateCatalogService() {
        var service = CatalogService.create(tenantId, subjectId, subdivisionId, "Old", 30);
        var previousUpdatedAt = service.getUpdatedAt();
        Map<String, Object> fields = new HashMap<>();
        fields.put("key", "value");

        service.update("New", "Desc", 45, Priority.HIGH, true, true, fields);

        assertThat(service.getDisplayName()).isEqualTo("New");
        assertThat(service.getDescription()).isEqualTo("Desc");
        assertThat(service.getSlaDays()).isEqualTo(45);
        assertThat(service.getDefaultPriority()).isEqualTo(Priority.HIGH);
        assertThat(service.isAllowsAnonymous()).isTrue();
        assertThat(service.isAllowsMultiForward()).isTrue();
        assertThat(service.getDynamicFields()).containsEntry("key", "value");
        assertThat(service.getUpdatedAt()).isAfterOrEqualTo(previousUpdatedAt);
    }

    @Test
    void shouldFailUpdateWithNegativeSlaDays() {
        var service = CatalogService.create(tenantId, subjectId, subdivisionId, "X", 10);

        assertThatThrownBy(() -> service.update(null, null, -1, Priority.NORMAL, false, false, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("slaDays must not be negative");
    }

    @Test
    void shouldFailUpdateWithNullDefaultPriority() {
        var service = CatalogService.create(tenantId, subjectId, subdivisionId, "X", 10);

        assertThatThrownBy(() -> service.update("A", null, 5, null, false, false, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("defaultPriority must not be null");
    }

    @Test
    void shouldReconstitute() {
        var id = UUID.randomUUID();
        var deptId = UUID.randomUUID();
        var created = Instant.parse("2024-01-01T00:00:00Z");
        var updated = Instant.parse("2024-01-02T00:00:00Z");
        Map<String, Object> dynamic = Map.of("a", 1);

        var service = CatalogService.reconstitute(
            id, tenantId, subjectId, subdivisionId, deptId, "Name", "Desc", 20,
            Priority.URGENT, true, false, true, false, dynamic, false, created, updated);

        assertThat(service.getId()).isEqualTo(id);
        assertThat(service.getTenantId()).isEqualTo(tenantId);
        assertThat(service.getSubjectId()).isEqualTo(subjectId);
        assertThat(service.getSubdivisionId()).isEqualTo(subdivisionId);
        assertThat(service.getDepartmentId()).isEqualTo(deptId);
        assertThat(service.getDisplayName()).isEqualTo("Name");
        assertThat(service.getDescription()).isEqualTo("Desc");
        assertThat(service.getSlaDays()).isEqualTo(20);
        assertThat(service.getDefaultPriority()).isEqualTo(Priority.URGENT);
        assertThat(service.isAllowsAnonymous()).isTrue();
        assertThat(service.isAllowsMultiForward()).isFalse();
        assertThat(service.isVisibleWeb()).isTrue();
        assertThat(service.isVisibleApp()).isFalse();
        assertThat(service.getDynamicFields()).isEqualTo(dynamic);
        assertThat(service.isActive()).isFalse();
        assertThat(service.getCreatedAt()).isEqualTo(created);
        assertThat(service.getUpdatedAt()).isEqualTo(updated);
    }
}
