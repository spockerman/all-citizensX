package com.allcitizens.domain.address;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddressTest {

    private final UUID cityId = UUID.randomUUID();

    @Test
    void shouldCreateValidAddress() {
        var districtId = UUID.randomUUID();
        var address = Address.create(
            cityId, "SP", "01310100", "Av. Paulista", "1000", "Sala 1", districtId, "MASP", -23.56, -46.65
        );

        assertThat(address.getId()).isNotNull();
        assertThat(address.getCityId()).isEqualTo(cityId);
        assertThat(address.getStateCode()).isEqualTo("SP");
        assertThat(address.getZipCode()).isEqualTo("01310100");
        assertThat(address.getStreet()).isEqualTo("Av. Paulista");
        assertThat(address.getNumber()).isEqualTo("1000");
        assertThat(address.getComplement()).isEqualTo("Sala 1");
        assertThat(address.getDistrictId()).isEqualTo(districtId);
        assertThat(address.getLandmark()).isEqualTo("MASP");
        assertThat(address.getLatitude()).isEqualTo(-23.56);
        assertThat(address.getLongitude()).isEqualTo(-46.65);
        assertThat(address.getCreatedAt()).isNotNull();
        assertThat(address.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldFailWhenCityIdIsNull() {
        assertThatThrownBy(() -> Address.create(
            null, "SP", null, null, null, null, null, null, null, null
        ))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("cityId must not be null");
    }

    @Test
    void shouldFailWhenStateCodeIsBlank() {
        assertThatThrownBy(() -> Address.create(
            cityId, "", null, null, null, null, null, null, null, null
        ))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("stateCode must not be blank");
    }

    @Test
    void shouldFailWhenStateCodeIsNull() {
        assertThatThrownBy(() -> Address.create(
            cityId, null, null, null, null, null, null, null, null, null
        ))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("stateCode must not be blank");
    }

    @Test
    void shouldUpdateAddress() {
        var address = Address.create(cityId, "RJ", "20000", "Old St", "1", null, null, null, null, null);
        var previousUpdatedAt = address.getUpdatedAt();

        address.update("20100", "New St", "2", "Apto", UUID.randomUUID(), "Near park");

        assertThat(address.getZipCode()).isEqualTo("20100");
        assertThat(address.getStreet()).isEqualTo("New St");
        assertThat(address.getNumber()).isEqualTo("2");
        assertThat(address.getComplement()).isEqualTo("Apto");
        assertThat(address.getLandmark()).isEqualTo("Near park");
        assertThat(address.getUpdatedAt()).isAfterOrEqualTo(previousUpdatedAt);
    }

    @Test
    void shouldSetCoordinates() {
        var address = Address.create(cityId, "SP", null, null, null, null, null, null, null, null);

        address.setCoordinates(-22.9, -43.2);

        assertThat(address.getLatitude()).isEqualTo(-22.9);
        assertThat(address.getLongitude()).isEqualTo(-43.2);
    }

    @Test
    void shouldReconstitute() {
        var id = UUID.randomUUID();
        var districtId = UUID.randomUUID();
        var created = Instant.parse("2024-01-01T12:00:00Z");
        var updated = Instant.parse("2024-01-02T12:00:00Z");

        var address = Address.reconstitute(
            id, "01000", "Rua A", "10", "Bloco B", districtId, cityId, "SP", "Praça", 1.0, 2.0,
            created, updated
        );

        assertThat(address.getId()).isEqualTo(id);
        assertThat(address.getZipCode()).isEqualTo("01000");
        assertThat(address.getStreet()).isEqualTo("Rua A");
        assertThat(address.getNumber()).isEqualTo("10");
        assertThat(address.getComplement()).isEqualTo("Bloco B");
        assertThat(address.getDistrictId()).isEqualTo(districtId);
        assertThat(address.getCityId()).isEqualTo(cityId);
        assertThat(address.getStateCode()).isEqualTo("SP");
        assertThat(address.getLandmark()).isEqualTo("Praça");
        assertThat(address.getLatitude()).isEqualTo(1.0);
        assertThat(address.getLongitude()).isEqualTo(2.0);
        assertThat(address.getCreatedAt()).isEqualTo(created);
        assertThat(address.getUpdatedAt()).isEqualTo(updated);
    }
}
