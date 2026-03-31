package com.allcitizens.infrastructure.adapter.outbound.persistence.person.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.person.entity.IndividualJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaIndividualRepository extends JpaRepository<IndividualJpaEntity, UUID> {

    Optional<IndividualJpaEntity> findByPersonId(UUID personId);

    void deleteByPersonId(UUID personId);

    List<IndividualJpaEntity> findByTaxId(String taxId);

    List<IndividualJpaEntity> findAllByPersonIdIn(Collection<UUID> personIds);
}
