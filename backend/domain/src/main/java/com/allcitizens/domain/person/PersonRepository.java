package com.allcitizens.domain.person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository {

    Person save(Person person);

    Optional<Person> findById(UUID id);

    List<Person> findAllByTenantId(UUID tenantId);

    Optional<Person> findByTenantIdAndEmail(UUID tenantId, String email);

    Optional<Person> findByTenantIdAndTaxId(UUID tenantId, String taxId);

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
