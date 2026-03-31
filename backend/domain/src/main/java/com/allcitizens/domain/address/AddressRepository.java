package com.allcitizens.domain.address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository {

    Address save(Address address);

    Optional<Address> findById(UUID id);

    List<Address> findAll();

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
