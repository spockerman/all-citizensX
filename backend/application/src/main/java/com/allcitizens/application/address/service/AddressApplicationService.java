package com.allcitizens.application.address.service;

import com.allcitizens.application.address.command.CreateAddressCommand;
import com.allcitizens.application.address.command.UpdateAddressCommand;
import com.allcitizens.application.address.result.AddressResult;
import com.allcitizens.domain.address.Address;
import com.allcitizens.domain.address.AddressRepository;
import com.allcitizens.domain.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.UUID;

public class AddressApplicationService {

    private final AddressRepository addressRepository;

    public AddressApplicationService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional
    public AddressResult create(CreateAddressCommand command) {
        var address = Address.create(
            command.cityId(),
            command.stateCode(),
            command.zipCode(),
            command.street(),
            command.number(),
            command.complement(),
            command.districtId(),
            command.landmark(),
            command.latitude(),
            command.longitude()
        );

        address = addressRepository.save(address);
        return AddressResult.fromDomain(address);
    }

    public AddressResult getById(UUID id) {
        var address = addressRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Address", id));
        return AddressResult.fromDomain(address);
    }

    @Transactional
    public AddressResult update(UUID id, UpdateAddressCommand command) {
        var address = addressRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Address", id));

        address.update(
            command.zipCode() != null ? command.zipCode() : address.getZipCode(),
            command.street() != null ? command.street() : address.getStreet(),
            command.number() != null ? command.number() : address.getNumber(),
            command.complement() != null ? command.complement() : address.getComplement(),
            command.districtId() != null ? command.districtId() : address.getDistrictId(),
            command.landmark() != null ? command.landmark() : address.getLandmark()
        );

        if (command.latitude() != null || command.longitude() != null) {
            var lat = command.latitude() != null ? command.latitude() : address.getLatitude();
            var lng = command.longitude() != null ? command.longitude() : address.getLongitude();
            address.setCoordinates(lat, lng);
        }

        address = addressRepository.save(address);
        return AddressResult.fromDomain(address);
    }

    @Transactional
    public void delete(UUID id) {
        if (!addressRepository.existsById(id)) {
            throw new EntityNotFoundException("Address", id);
        }
        addressRepository.deleteById(id);
    }
}
