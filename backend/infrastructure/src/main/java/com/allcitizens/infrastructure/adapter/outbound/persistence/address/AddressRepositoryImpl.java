package com.allcitizens.infrastructure.adapter.outbound.persistence.address;

import com.allcitizens.domain.address.Address;
import com.allcitizens.domain.address.AddressRepository;
import com.allcitizens.infrastructure.adapter.outbound.persistence.address.mapper.AddressPersistenceMapper;
import com.allcitizens.infrastructure.adapter.outbound.persistence.address.repository.JpaAddressRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AddressRepositoryImpl implements AddressRepository {

    private final JpaAddressRepository jpaRepository;
    private final AddressPersistenceMapper mapper;

    public AddressRepositoryImpl(JpaAddressRepository jpaRepository,
                                 AddressPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Address save(Address address) {
        var entity = mapper.toEntity(address);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Address> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Address> findAll() {
        return jpaRepository.findAll().stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
