package com.allcitizens.application.address.service;

import com.allcitizens.application.address.command.CreateAddressCommand;
import com.allcitizens.application.address.command.UpdateAddressCommand;
import com.allcitizens.domain.address.Address;
import com.allcitizens.domain.address.AddressRepository;
import com.allcitizens.domain.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressApplicationServiceTest {

    @Mock
    private AddressRepository addressRepository;

    private AddressApplicationService service;

    private final UUID cityId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        service = new AddressApplicationService(addressRepository);
    }

    @Test
    void shouldCreateAddress() {
        var command = new CreateAddressCommand(
            cityId, "SP", "01310100", "Av. Paulista", "1000", null, null, null, null, null
        );

        when(addressRepository.save(any(Address.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.create(command);

        assertThat(result.cityId()).isEqualTo(cityId);
        assertThat(result.stateCode()).isEqualTo("SP");
        assertThat(result.zipCode()).isEqualTo("01310100");
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    void shouldGetAddressById() {
        var id = UUID.randomUUID();
        var address = Address.create(cityId, "RJ", null, "Street", "1", null, null, null, null, null);

        when(addressRepository.findById(id)).thenReturn(Optional.of(address));

        var result = service.getById(id);

        assertThat(result.street()).isEqualTo("Street");
    }

    @Test
    void shouldThrowWhenAddressNotFound() {
        var id = UUID.randomUUID();
        when(addressRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldUpdateAddress() {
        var id = UUID.randomUUID();
        var address = Address.create(cityId, "SP", "01000", "Old", "1", null, null, null, null, null);
        var command = new UpdateAddressCommand(
            "02000", "New", "2", "Apto", null, "Land", null, null
        );

        when(addressRepository.findById(id)).thenReturn(Optional.of(address));
        when(addressRepository.save(any(Address.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.update(id, command);

        assertThat(result.zipCode()).isEqualTo("02000");
        assertThat(result.street()).isEqualTo("New");
        assertThat(result.number()).isEqualTo("2");
        assertThat(result.complement()).isEqualTo("Apto");
        assertThat(result.landmark()).isEqualTo("Land");
    }

    @Test
    void shouldDeleteAddress() {
        var id = UUID.randomUUID();
        when(addressRepository.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(addressRepository).deleteById(id);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentAddress() {
        var id = UUID.randomUUID();
        when(addressRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(id))
            .isInstanceOf(EntityNotFoundException.class);
        verify(addressRepository, never()).deleteById(any());
    }
}
