package com.allcitizens.domain.person;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PersonTest {

    private final UUID tenantId = UUID.randomUUID();

    @Test
    void shouldCreateIndividual() {
        var person = Person.createIndividual(tenantId, "Jane Doe", "jane@example.com", "12345678901234");

        assertThat(person.getId()).isNotNull();
        assertThat(person.getTenantId()).isEqualTo(tenantId);
        assertThat(person.getType()).isEqualTo(PersonType.INDIVIDUAL);
        assertThat(person.getFullName()).isEqualTo("Jane Doe");
        assertThat(person.getEmail()).isEqualTo("jane@example.com");
        assertThat(person.getTaxId()).isEqualTo("12345678901234");
        assertThat(person.getGender()).isEqualTo(Gender.NOT_INFORMED);
        assertThat(person.isActive()).isTrue();
        assertThat(person.getCreatedAt()).isNotNull();
        assertThat(person.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldRejectNullTenantId() {
        assertThatThrownBy(() -> Person.createIndividual(null, "Name", null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("tenantId");
    }

    @Test
    void shouldRejectBlankFullName() {
        assertThatThrownBy(() -> Person.createIndividual(tenantId, "   ", null, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("fullName");
    }

    @Test
    void shouldUpdate() {
        var person = Person.createIndividual(tenantId, "Old", "old@x.com", null);
        var birth = LocalDate.of(1990, 1, 15);

        person.update("New Name", "new@x.com", "999", "Mother", birth, Gender.FEMALE);

        assertThat(person.getFullName()).isEqualTo("New Name");
        assertThat(person.getEmail()).isEqualTo("new@x.com");
        assertThat(person.getTaxId()).isEqualTo("999");
        assertThat(person.getMotherName()).isEqualTo("Mother");
        assertThat(person.getBirthDate()).isEqualTo(birth);
        assertThat(person.getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    void shouldAssignAddress() {
        var person = Person.createIndividual(tenantId, "A", null, null);
        var addressId = UUID.randomUUID();

        person.assignAddress(addressId);

        assertThat(person.getAddressId()).isEqualTo(addressId);
    }

    @Test
    void shouldRejectNullAddressId() {
        var person = Person.createIndividual(tenantId, "A", null, null);

        assertThatThrownBy(() -> person.assignAddress(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldRemoveAddress() {
        var person = Person.createIndividual(tenantId, "A", null, null);
        person.assignAddress(UUID.randomUUID());

        person.removeAddress();

        assertThat(person.getAddressId()).isNull();
    }

    @Test
    void shouldLinkKeycloakUser() {
        var person = Person.createIndividual(tenantId, "A", null, null);
        var kid = UUID.randomUUID();

        person.linkKeycloakUser(kid);

        assertThat(person.getKeycloakUserId()).isEqualTo(kid);
    }

    @Test
    void shouldRejectNullKeycloakUserId() {
        var person = Person.createIndividual(tenantId, "A", null, null);

        assertThatThrownBy(() -> person.linkKeycloakUser(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldActivateAndDeactivate() {
        var person = Person.createIndividual(tenantId, "A", null, null);

        person.deactivate();
        assertThat(person.isActive()).isFalse();

        person.activate();
        assertThat(person.isActive()).isTrue();
    }

    @Test
    void shouldReconstitute() {
        var id = UUID.randomUUID();
        var addressId = UUID.randomUUID();
        var keycloakId = UUID.randomUUID();
        var created = Instant.parse("2024-01-01T10:00:00Z");
        var updated = Instant.parse("2024-01-02T10:00:00Z");
        var birth = LocalDate.of(1980, 5, 5);

        var person = Person.reconstitute(
            id, tenantId, PersonType.INDIVIDUAL, "e@e.com", addressId, keycloakId, true,
            "Full", "tax", "mom", birth, Gender.MALE, created, updated
        );

        assertThat(person.getId()).isEqualTo(id);
        assertThat(person.getTenantId()).isEqualTo(tenantId);
        assertThat(person.getType()).isEqualTo(PersonType.INDIVIDUAL);
        assertThat(person.getEmail()).isEqualTo("e@e.com");
        assertThat(person.getAddressId()).isEqualTo(addressId);
        assertThat(person.getKeycloakUserId()).isEqualTo(keycloakId);
        assertThat(person.isActive()).isTrue();
        assertThat(person.getFullName()).isEqualTo("Full");
        assertThat(person.getTaxId()).isEqualTo("tax");
        assertThat(person.getMotherName()).isEqualTo("mom");
        assertThat(person.getBirthDate()).isEqualTo(birth);
        assertThat(person.getGender()).isEqualTo(Gender.MALE);
        assertThat(person.getCreatedAt()).isEqualTo(created);
        assertThat(person.getUpdatedAt()).isEqualTo(updated);
    }
}
