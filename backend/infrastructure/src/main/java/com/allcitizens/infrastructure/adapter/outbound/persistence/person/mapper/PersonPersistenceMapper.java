package com.allcitizens.infrastructure.adapter.outbound.persistence.person.mapper;

import com.allcitizens.domain.person.Gender;
import com.allcitizens.domain.person.Person;
import com.allcitizens.domain.person.PersonType;
import com.allcitizens.infrastructure.adapter.outbound.persistence.person.entity.IndividualJpaEntity;
import com.allcitizens.infrastructure.adapter.outbound.persistence.person.entity.PersonJpaEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PersonPersistenceMapper {

    public Person toDomain(PersonJpaEntity personEntity, IndividualJpaEntity individualEntity) {
        PersonType type = PersonType.valueOf(personEntity.getType());
        String fullName = individualEntity != null ? individualEntity.getFullName() : null;
        String taxId = individualEntity != null ? individualEntity.getTaxId() : null;
        String motherName = individualEntity != null ? individualEntity.getMotherName() : null;
        var birthDate = individualEntity != null ? individualEntity.getBirthDate() : null;
        Gender gender = mapGender(individualEntity);

        return Person.reconstitute(
            personEntity.getId(),
            personEntity.getTenantId(),
            type,
            personEntity.getEmail(),
            personEntity.getAddressId(),
            personEntity.getKeycloakUserId(),
            personEntity.isActive(),
            fullName,
            taxId,
            motherName,
            birthDate,
            gender,
            personEntity.getCreatedAt(),
            personEntity.getUpdatedAt()
        );
    }

    private static Gender mapGender(IndividualJpaEntity individualEntity) {
        if (individualEntity == null || individualEntity.getGender() == null
            || individualEntity.getGender().isBlank()) {
            return Gender.NOT_INFORMED;
        }
        return Gender.valueOf(individualEntity.getGender());
    }

    public PersonJpaEntity toPersonEntity(Person domain) {
        var entity = new PersonJpaEntity();
        entity.setId(domain.getId());
        entity.setTenantId(domain.getTenantId());
        entity.setType(domain.getType().name());
        entity.setEmail(domain.getEmail());
        entity.setAddressId(domain.getAddressId());
        entity.setKeycloakUserId(domain.getKeycloakUserId());
        entity.setActive(domain.isActive());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public IndividualJpaEntity toIndividualEntity(Person domain, UUID individualId) {
        var entity = new IndividualJpaEntity();
        entity.setId(individualId != null ? individualId : UUID.randomUUID());
        entity.setPersonId(domain.getId());
        entity.setFullName(domain.getFullName());
        entity.setTaxId(domain.getTaxId());
        entity.setMotherName(domain.getMotherName());
        entity.setBirthDate(domain.getBirthDate());
        entity.setGender(domain.getGender() != null ? domain.getGender().name() : Gender.NOT_INFORMED.name());
        return entity;
    }
}
