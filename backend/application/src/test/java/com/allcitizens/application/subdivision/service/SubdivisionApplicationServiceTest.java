package com.allcitizens.application.subdivision.service;

import com.allcitizens.application.subdivision.command.CreateSubdivisionCommand;
import com.allcitizens.application.subdivision.query.ListSubdivisionsQuery;
import com.allcitizens.application.subdivision.command.UpdateSubdivisionCommand;
import com.allcitizens.domain.exception.EntityNotFoundException;
import com.allcitizens.domain.common.PageResult;
import com.allcitizens.domain.subdivision.Subdivision;
import com.allcitizens.domain.subdivision.SubdivisionRepository;
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
class SubdivisionApplicationServiceTest {

    @Mock
    private SubdivisionRepository subdivisionRepository;

    private SubdivisionApplicationService service;

    @BeforeEach
    void setUp() {
        service = new SubdivisionApplicationService(subdivisionRepository);
    }

    @Test
    void shouldCreateSubdivision() {
        var command = new CreateSubdivisionCommand("North");

        when(subdivisionRepository.save(any(Subdivision.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.create(command);

        assertThat(result.name()).isEqualTo("North");
        assertThat(result.active()).isTrue();
        verify(subdivisionRepository).save(any(Subdivision.class));
    }

    @Test
    void shouldGetSubdivisionById() {
        var id = UUID.randomUUID();
        var subdivision = Subdivision.create("South");

        when(subdivisionRepository.findById(id)).thenReturn(Optional.of(subdivision));

        var result = service.getById(id);

        assertThat(result.name()).isEqualTo("South");
    }

    @Test
    void shouldThrowWhenSubdivisionNotFound() {
        var id = UUID.randomUUID();
        when(subdivisionRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldUpdateSubdivision() {
        var id = UUID.randomUUID();
        var subdivision = Subdivision.create("Old");
        var command = new UpdateSubdivisionCommand("New Name", false);

        when(subdivisionRepository.findById(id)).thenReturn(Optional.of(subdivision));
        when(subdivisionRepository.save(any(Subdivision.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.update(id, command);

        assertThat(result.name()).isEqualTo("New Name");
        assertThat(result.active()).isFalse();
    }

    @Test
    void shouldDeleteSubdivision() {
        var id = UUID.randomUUID();
        when(subdivisionRepository.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(subdivisionRepository).deleteById(id);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentSubdivision() {
        var id = UUID.randomUUID();
        when(subdivisionRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(id))
            .isInstanceOf(EntityNotFoundException.class);
        verify(subdivisionRepository, never()).deleteById(any());
    }

    @Test
    void shouldListAllSubdivisions() {
        var a = Subdivision.create("A");
        var b = Subdivision.create("B");

        when(subdivisionRepository.findAllPaged(0, 20))
            .thenReturn(new PageResult<>(List.of(a, b), 2, 0, 20));

        var results = service.execute(new ListSubdivisionsQuery(0, 20, null));

        assertThat(results.content()).hasSize(2);
        assertThat(results.content().get(0).name()).isEqualTo("A");
        assertThat(results.content().get(1).name()).isEqualTo("B");
    }
}
