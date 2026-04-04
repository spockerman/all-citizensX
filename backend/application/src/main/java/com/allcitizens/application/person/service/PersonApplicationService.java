package com.allcitizens.application.person.service;

import com.allcitizens.application.person.command.CreatePersonCommand;
import com.allcitizens.application.person.command.UpdatePersonCommand;
import com.allcitizens.application.person.query.ListPersonsQuery;
import com.allcitizens.application.person.result.PersonResult;
import com.allcitizens.application.person.usecase.ListPersonsUseCase;
import com.allcitizens.domain.common.PageResult;
import com.allcitizens.domain.exception.EntityNotFoundException;
import com.allcitizens.domain.person.Gender;
import com.allcitizens.domain.person.Person;
import com.allcitizens.domain.person.PersonRepository;
import jakarta.transaction.Transactional;

import java.util.UUID;

public class PersonApplicationService implements ListPersonsUseCase {

    private final PersonRepository personRepository;

    public PersonApplicationService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional
    public PersonResult create(CreatePersonCommand command) {
        var person = Person.createIndividual(
            command.tenantId(),
            command.fullName(),
            command.email(),
            command.taxId()
        );
        Gender gender = resolveGender(command.gender());
        person.update(
            command.fullName(),
            command.email(),
            command.taxId(),
            command.motherName(),
            command.birthDate(),
            gender
        );
        if (command.addressId() != null) {
            person.assignAddress(command.addressId());
        }
        person = personRepository.save(person);
        return PersonResult.fromDomain(person);
    }

    public PersonResult getById(UUID id) {
        var person = personRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Person", id));
        return PersonResult.fromDomain(person);
    }

    @Transactional
    public PersonResult update(UUID id, UpdatePersonCommand command) {
        var person = personRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Person", id));

        Gender gender = command.gender() != null
            ? resolveGender(command.gender())
            : person.getGender();

        person.update(
            command.fullName() != null ? command.fullName() : person.getFullName(),
            command.email() != null ? command.email() : person.getEmail(),
            command.taxId() != null ? command.taxId() : person.getTaxId(),
            command.motherName() != null ? command.motherName() : person.getMotherName(),
            command.birthDate() != null ? command.birthDate() : person.getBirthDate(),
            gender
        );

        if (command.addressId() != null) {
            person.assignAddress(command.addressId());
        }

        if (command.active() != null) {
            if (command.active()) {
                person.activate();
            } else {
                person.deactivate();
            }
        }

        person = personRepository.save(person);
        return PersonResult.fromDomain(person);
    }

    @Override
    public PageResult<PersonResult> execute(ListPersonsQuery query) {
        var page = (query.search() == null || query.search().isBlank())
                ? personRepository.findAllByTenantIdPaged(query.tenantId(), query.page(), query.size())
                : personRepository.searchByTenantIdPaged(
                        query.tenantId(), query.search().trim(), query.page(), query.size());
        var content = page.content().stream().map(PersonResult::fromDomain).toList();
        return new PageResult<>(content, page.totalElements(), page.page(), page.size());
    }

    @Transactional
    public void delete(UUID id) {
        if (!personRepository.existsById(id)) {
            throw new EntityNotFoundException("Person", id);
        }
        personRepository.deleteById(id);
    }

    private static Gender resolveGender(String gender) {
        if (gender == null || gender.isBlank()) {
            return Gender.NOT_INFORMED;
        }
        return Gender.valueOf(gender.trim().toUpperCase());
    }
}
