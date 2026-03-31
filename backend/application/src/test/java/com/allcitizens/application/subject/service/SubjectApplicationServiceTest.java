package com.allcitizens.application.subject.service;

import com.allcitizens.application.subject.command.CreateSubjectCommand;
import com.allcitizens.application.subject.command.UpdateSubjectCommand;
import com.allcitizens.domain.exception.EntityNotFoundException;
import com.allcitizens.domain.subject.Subject;
import com.allcitizens.domain.subject.SubjectRepository;
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
class SubjectApplicationServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    private SubjectApplicationService service;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        service = new SubjectApplicationService(subjectRepository);
    }

    @Test
    void shouldCreateSubject() {
        var departmentId = UUID.randomUUID();
        var command = new CreateSubjectCommand(
            tenantId, departmentId, "Permits", true, false
        );

        when(subjectRepository.save(any(Subject.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.create(command);

        assertThat(result.name()).isEqualTo("Permits");
        assertThat(result.tenantId()).isEqualTo(tenantId);
        assertThat(result.departmentId()).isEqualTo(departmentId);
        assertThat(result.visibleWeb()).isTrue();
        assertThat(result.visibleApp()).isFalse();
        assertThat(result.active()).isTrue();
        verify(subjectRepository).save(any(Subject.class));
    }

    @Test
    void shouldCreateSubjectWithoutDepartment() {
        var command = new CreateSubjectCommand(
            tenantId, null, "General", true, true
        );

        when(subjectRepository.save(any(Subject.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.create(command);

        assertThat(result.departmentId()).isNull();
        assertThat(result.name()).isEqualTo("General");
    }

    @Test
    void shouldGetSubjectById() {
        var id = UUID.randomUUID();
        var subject = Subject.create(tenantId, "Taxes");

        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));

        var result = service.getById(id);

        assertThat(result.name()).isEqualTo("Taxes");
    }

    @Test
    void shouldThrowWhenSubjectNotFound() {
        var id = UUID.randomUUID();
        when(subjectRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldUpdateSubject() {
        var id = UUID.randomUUID();
        var newDept = UUID.randomUUID();
        var subject = Subject.create(tenantId, "Old");
        var command = new UpdateSubjectCommand("New Name", newDept, true, false, null);

        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));
        when(subjectRepository.save(any(Subject.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.update(id, command);

        assertThat(result.name()).isEqualTo("New Name");
        assertThat(result.departmentId()).isEqualTo(newDept);
        assertThat(result.visibleWeb()).isFalse();
    }

    @Test
    void shouldDeleteSubject() {
        var id = UUID.randomUUID();
        when(subjectRepository.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(subjectRepository).deleteById(id);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentSubject() {
        var id = UUID.randomUUID();
        when(subjectRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(id))
            .isInstanceOf(EntityNotFoundException.class);
        verify(subjectRepository, never()).deleteById(any());
    }

    @Test
    void shouldListSubjectsByTenantId() {
        var s1 = Subject.create(tenantId, "A");
        var s2 = Subject.create(tenantId, "B");

        when(subjectRepository.findAllByTenantId(tenantId)).thenReturn(List.of(s1, s2));

        var results = service.listByTenantId(tenantId);

        assertThat(results).hasSize(2);
        assertThat(results.get(0).name()).isEqualTo("A");
        assertThat(results.get(1).name()).isEqualTo("B");
    }
}
