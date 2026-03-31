package com.allcitizens.infrastructure.adapter.outbound.persistence.address.mapper;

import com.allcitizens.domain.address.Address;
import com.allcitizens.infrastructure.adapter.outbound.persistence.address.entity.AddressJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class AddressPersistenceMapper {

    public Address toDomain(AddressJpaEntity entity) {
        return Address.reconstitute(
            entity.getId(),
            entity.getZipCode(),
            entity.getStreet(),
            entity.getNumber(),
            entity.getComplement(),
            entity.getDistrictId(),
            entity.getCityId(),
            entity.getStateCode(),
            entity.getLandmark(),
            entity.getLatitude(),
            entity.getLongitude(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public AddressJpaEntity toEntity(Address address) {
        var entity = new AddressJpaEntity();
        entity.setId(address.getId());
        entity.setZipCode(address.getZipCode());
        entity.setStreet(address.getStreet());
        entity.setNumber(address.getNumber());
        entity.setComplement(address.getComplement());
        entity.setDistrictId(address.getDistrictId());
        entity.setCityId(address.getCityId());
        entity.setStateCode(address.getStateCode());
        entity.setLandmark(address.getLandmark());
        entity.setLatitude(address.getLatitude());
        entity.setLongitude(address.getLongitude());
        entity.setCreatedAt(address.getCreatedAt());
        entity.setUpdatedAt(address.getUpdatedAt());
        return entity;
    }
}
