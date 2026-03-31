package com.allcitizens.infrastructure.adapter.outbound.persistence.address.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.address.entity.AddressJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaAddressRepository extends JpaRepository<AddressJpaEntity, UUID> {
}
