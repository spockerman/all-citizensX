package com.allcitizens.application.person.service;

import com.allcitizens.application.person.command.CreatePersonCommand;
import com.allcitizens.application.person.query.ListPersonsQuery;
import com.allcitizens.application.person.command.UpdatePersonCommand;
import com.allcitizens.domain.exception.EntityNotFoundException;
import com.allcitizens.domain.common.PageResult;
import com.allcitizens.domain.person.Person;
import com.allcitizens.domain.person.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
class PersonApplicationServiceTest {

    @Mock
    private PersonRepository personRepository;

    private PersonApplicationService service;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        service = new PersonApplicationService(personRepository);
    }

    @Test
    void shouldCreatePerson() {
        var command = new CreatePersonCommand(
            tenantId, "John", "j@x.com", "111", null, null, null, null
        );

        when(personRepository.save(any(Person.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.create(command);

        assertThat(result.fullName()).isEqualTo("John");
        assertThat(result.tenantId()).isEqualTo(tenantId);
        assertThat(result.email()).isEqualTo("j@x.com");
        assertThat(result.taxId()).isEqualTo("111");
        assertThat(result.type()).isEqualTo("INDIVIDUAL");
        verify(personRepository).save(any(Person.class));
    }

    @Test
    void shouldCreatePersonWithAddressAndDetails() {
        var addressId = UUID.randomUUID();
        var birth = LocalDate.of(1991, 3, 3);
        var command = new CreatePersonCommand(
            tenantId, "Mary", "m@x.com", "222", "Mom", birth, "FEMALE", addressId
        );

        when(personRepository.save(any(Person.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.create(command);

        assertThat(result.motherName()).isEqualTo("Mom");
        assertThat(result.birthDate()).isEqualTo(birth);
        assertThat(result.gender()).isEqualTo("FEMALE");
        assertThat(result.addressId()).isEqualTo(addressId);
    }

    @Test
    void shouldGetPersonById() {
        var id = UUID.randomUUID();
        var person = Person.createIndividual(tenantId, "A", null, null);

        when(personRepository.findById(id)).thenReturn(Optional.of(person));

        var result = service.getById(id);

        assertThat(result.fullName()).isEqualTo("A");
    }

    @Test
    void shouldThrowWhenPersonNotFoundOnGet() {
        var id = UUID.randomUUID();
        when(personRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldUpdatePerson() {
        var id = UUID.randomUUID();
        var person = Person.createIndividual(tenantId, "Old", "o@x.com", "1");
        var command = new UpdatePersonCommand(
            "New", "n@x.com", "2", "Mother", LocalDate.of(2000, 1, 1), "MALE", null, true
        );

        when(personRepository.findById(id)).thenReturn(Optional.of(person));
        when(personRepository.save(any(Person.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.update(id, command);

        assertThat(result.fullName()).isEqualTo("New");
        assertThat(result.email()).isEqualTo("n@x.com");
        assertThat(result.taxId()).isEqualTo("2");
        assertThat(result.gender()).isEqualTo("MALE");
        assertThat(result.active()).isTrue();
    }

    @Test
    void shouldDeletePerson() {
        var id = UUID.randomUUID();
        when(personRepository.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(personRepository).deleteById(id);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentPerson() {
        var id = UUID.randomUUID();
        when(personRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(id))
            .isInstanceOf(EntityNotFoundException.class);
        verify(personRepository, never()).deleteById(any());
    }

    @Test
    void shouldListPersonsByTenantId() {
        var p1 = Person.createIndividual(tenantId, "A", null, null);
        var p2 = Person.createIndividual(tenantId, "B", null, null);

        when(personRepository.findAllByTenantIdPaged(tenantId, 0, 20))
            .thenReturn(new PageResult<>(List.of(p1, p2), 2, 0, 20));

        var results = service.execute(new ListPersonsQuery(tenantId, 0, 20, null));

        assertThat(results.content()).hasSize(2);
        assertThat(results.content().get(0).fullName()).isEqualTo("A");
        assertThat(results.content().get(1).fullName()).isEqualTo("B");
    }
}
