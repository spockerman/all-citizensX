package com.allcitizens.infrastructure.adapter.outbound.persistence.person;

import com.allcitizens.domain.common.PageResult;
import com.allcitizens.domain.person.Person;
import com.allcitizens.domain.person.PersonRepository;
import com.allcitizens.domain.person.PersonType;
import com.allcitizens.infrastructure.adapter.outbound.persistence.person.entity.IndividualJpaEntity;
import com.allcitizens.infrastructure.adapter.outbound.persistence.person.entity.PersonJpaEntity;
import com.allcitizens.infrastructure.adapter.outbound.persistence.person.mapper.PersonPersistenceMapper;
import com.allcitizens.infrastructure.adapter.outbound.persistence.person.repository.JpaIndividualRepository;
import com.allcitizens.infrastructure.adapter.outbound.persistence.person.repository.JpaPersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class PersonRepositoryImpl implements PersonRepository {

    private final JpaPersonRepository jpaPersonRepository;
    private final JpaIndividualRepository jpaIndividualRepository;
    private final PersonPersistenceMapper mapper;

    public PersonRepositoryImpl(JpaPersonRepository jpaPersonRepository,
                                  JpaIndividualRepository jpaIndividualRepository,
                                  PersonPersistenceMapper mapper) {
        this.jpaPersonRepository = jpaPersonRepository;
        this.jpaIndividualRepository = jpaIndividualRepository;
        this.mapper = mapper;
    }

    @Override
    public Person save(Person person) {
        var personEntity = mapper.toPersonEntity(person);
        var savedPerson = jpaPersonRepository.save(personEntity);

        if (person.getType() == PersonType.INDIVIDUAL) {
            UUID existingIndividualId = jpaIndividualRepository.findByPersonId(savedPerson.getId())
                .map(IndividualJpaEntity::getId)
                .orElse(null);
            var individualEntity = mapper.toIndividualEntity(person, existingIndividualId);
            individualEntity.setPersonId(savedPerson.getId());
            jpaIndividualRepository.save(individualEntity);
        }

        return mapToDomain(savedPerson);
    }

    @Override
    public Optional<Person> findById(UUID id) {
        return jpaPersonRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<Person> findAllByTenantId(UUID tenantId) {
        List<PersonJpaEntity> persons = jpaPersonRepository.findAllByTenantId(tenantId);
        return mapPersonsWithIndividuals(persons);
    }

    @Override
    public PageResult<Person> findAllByTenantIdPaged(UUID tenantId, int page, int size) {
        var pg = jpaPersonRepository.findAllByTenantId(tenantId, PageRequest.of(page, size));
        return toPersonPageResult(pg);
    }

    @Override
    public PageResult<Person> searchByTenantIdPaged(UUID tenantId, String query, int page, int size) {
        var pg = jpaPersonRepository.searchByTenantId(tenantId, query, PageRequest.of(page, size));
        return toPersonPageResult(pg);
    }

    private PageResult<Person> toPersonPageResult(Page<PersonJpaEntity> page) {
        var content = mapPersonsWithIndividuals(page.getContent());
        return new PageResult<>(content, page.getTotalElements(), page.getNumber(), page.getSize());
    }

    private List<Person> mapPersonsWithIndividuals(List<PersonJpaEntity> persons) {
        if (persons.isEmpty()) {
            return List.of();
        }
        List<UUID> personIds = persons.stream().map(PersonJpaEntity::getId).toList();
        Map<UUID, IndividualJpaEntity> individualsByPersonId = jpaIndividualRepository
            .findAllByPersonIdIn(personIds)
            .stream()
            .collect(Collectors.toMap(IndividualJpaEntity::getPersonId, Function.identity(), (a, b) -> a));

        return persons.stream()
            .map(p -> mapper.toDomain(p, individualsByPersonId.get(p.getId())))
            .toList();
    }

    @Override
    public Optional<Person> findByTenantIdAndEmail(UUID tenantId, String email) {
        return jpaPersonRepository.findByTenantIdAndEmail(tenantId, email).map(this::mapToDomain);
    }

    @Override
    public Optional<Person> findByTenantIdAndTaxId(UUID tenantId, String taxId) {
        List<IndividualJpaEntity> matches = jpaIndividualRepository.findByTaxId(taxId);
        for (IndividualJpaEntity ind : matches) {
            Optional<PersonJpaEntity> personOpt = jpaPersonRepository.findById(ind.getPersonId());
            if (personOpt.isPresent()) {
                PersonJpaEntity p = personOpt.get();
                if (p.getTenantId().equals(tenantId) && PersonType.INDIVIDUAL.name().equals(p.getType())) {
                    return Optional.of(mapper.toDomain(p, ind));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaPersonRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        jpaIndividualRepository.deleteByPersonId(id);
        jpaPersonRepository.deleteById(id);
    }

    private Person mapToDomain(PersonJpaEntity personEntity) {
        IndividualJpaEntity individual = null;
        if (PersonType.INDIVIDUAL.name().equals(personEntity.getType())) {
            individual = jpaIndividualRepository.findByPersonId(personEntity.getId()).orElse(null);
        }
        return mapper.toDomain(personEntity, individual);
    }
}
