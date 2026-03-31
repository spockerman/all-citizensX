package com.allcitizens.application.department.service;

import com.allcitizens.application.department.command.CreateDepartmentCommand;
import com.allcitizens.application.department.command.UpdateDepartmentCommand;
import com.allcitizens.domain.department.Department;
import com.allcitizens.domain.department.DepartmentRepository;
import com.allcitizens.domain.exception.EntityNotFoundException;
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
class DepartmentApplicationServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    private DepartmentApplicationService service;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        service = new DepartmentApplicationService(departmentRepository);
    }

    @Test
    void shouldCreateDepartment() {
        var command = new CreateDepartmentCommand(
            tenantId, null, "Engineering", "ENG", "eng@test.com", true, false, null, 1
        );

        when(departmentRepository.save(any(Department.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.create(command);

        assertThat(result.name()).isEqualTo("Engineering");
        assertThat(result.abbreviation()).isEqualTo("ENG");
        assertThat(result.tenantId()).isEqualTo(tenantId);
        assertThat(result.active()).isTrue();
        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    void shouldCreateDepartmentWithParent() {
        var parentId = UUID.randomUUID();
        var command = new CreateDepartmentCommand(
            tenantId, parentId, "Sub Department", null, null, false, false, null, 0
        );

        when(departmentRepository.existsById(parentId)).thenReturn(true);
        when(departmentRepository.save(any(Department.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.create(command);

        assertThat(result.parentId()).isEqualTo(parentId);
        verify(departmentRepository).existsById(parentId);
    }

    @Test
    void shouldFailCreateWhenParentNotFound() {
        var parentId = UUID.randomUUID();
        var command = new CreateDepartmentCommand(
            tenantId, parentId, "Sub Department", null, null, false, false, null, 0
        );

        when(departmentRepository.existsById(parentId)).thenReturn(false);

        assertThatThrownBy(() -> service.create(command))
            .isInstanceOf(EntityNotFoundException.class);
        verify(departmentRepository, never()).save(any());
    }

    @Test
    void shouldGetDepartmentById() {
        var id = UUID.randomUUID();
        var department = Department.create(tenantId, "Engineering", "ENG", null, false, false, 0);

        when(departmentRepository.findById(id)).thenReturn(Optional.of(department));

        var result = service.getById(id);

        assertThat(result.name()).isEqualTo("Engineering");
    }

    @Test
    void shouldThrowWhenDepartmentNotFound() {
        var id = UUID.randomUUID();
        when(departmentRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldUpdateDepartment() {
        var id = UUID.randomUUID();
        var department = Department.create(tenantId, "Old Name", "OLD", null, false, false, 0);
        var command = new UpdateDepartmentCommand("New Name", "NEW", "new@test.com", true, null, null, 5);

        when(departmentRepository.findById(id)).thenReturn(Optional.of(department));
        when(departmentRepository.save(any(Department.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.update(id, command);

        assertThat(result.name()).isEqualTo("New Name");
        assertThat(result.abbreviation()).isEqualTo("NEW");
        assertThat(result.email()).isEqualTo("new@test.com");
        assertThat(result.canRespond()).isTrue();
        assertThat(result.displayOrder()).isEqualTo(5);
    }

    @Test
    void shouldDeleteDepartment() {
        var id = UUID.randomUUID();
        when(departmentRepository.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(departmentRepository).deleteById(id);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentDepartment() {
        var id = UUID.randomUUID();
        when(departmentRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(id))
            .isInstanceOf(EntityNotFoundException.class);
        verify(departmentRepository, never()).deleteById(any());
    }

    @Test
    void shouldListDepartmentsByTenantId() {
        var dept1 = Department.create(tenantId, "Dept 1", null, null, false, false, 0);
        var dept2 = Department.create(tenantId, "Dept 2", null, null, false, false, 1);

        when(departmentRepository.findAllByTenantId(tenantId)).thenReturn(List.of(dept1, dept2));

        var results = service.listByTenantId(tenantId);

        assertThat(results).hasSize(2);
        assertThat(results.get(0).name()).isEqualTo("Dept 1");
        assertThat(results.get(1).name()).isEqualTo("Dept 2");
    }
}
