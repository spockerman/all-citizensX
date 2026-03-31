package com.allcitizens.domain.person;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Person {

    private UUID id;
    private UUID tenantId;
    private PersonType type;
    private String email;
    private UUID addressId;
    private UUID keycloakUserId;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    private String fullName;
    private String taxId;
    private String motherName;
    private LocalDate birthDate;
    private Gender gender;

    private Person() {
    }

    public static Person createIndividual(UUID tenantId, String fullName, String email, String taxId) {
        Objects.requireNonNull(tenantId, "tenantId must not be null");
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("fullName must not be blank");
        }

        var person = new Person();
        person.id = UUID.randomUUID();
        person.tenantId = tenantId;
        person.type = PersonType.INDIVIDUAL;
        person.email = email;
        person.addressId = null;
        person.keycloakUserId = null;
        person.active = true;
        person.createdAt = Instant.now();
        person.updatedAt = Instant.now();
        person.fullName = fullName;
        person.taxId = taxId;
        person.motherName = null;
        person.birthDate = null;
        person.gender = Gender.NOT_INFORMED;
        return person;
    }

    public static Person reconstitute(UUID id, UUID tenantId, PersonType type, String email, UUID addressId,
                                      UUID keycloakUserId, boolean active, String fullName, String taxId,
                                      String motherName, LocalDate birthDate, Gender gender,
                                      Instant createdAt, Instant updatedAt) {
        var person = new Person();
        person.id = id;
        person.tenantId = tenantId;
        person.type = type;
        person.email = email;
        person.addressId = addressId;
        person.keycloakUserId = keycloakUserId;
        person.active = active;
        person.fullName = fullName;
        person.taxId = taxId;
        person.motherName = motherName;
        person.birthDate = birthDate;
        person.gender = gender != null ? gender : Gender.NOT_INFORMED;
        person.createdAt = createdAt;
        person.updatedAt = updatedAt;
        return person;
    }

    public void update(String fullName, String email, String taxId, String motherName, LocalDate birthDate,
                       Gender gender) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("fullName must not be blank");
        }
        this.fullName = fullName;
        this.email = email;
        this.taxId = taxId;
        this.motherName = motherName;
        this.birthDate = birthDate;
        if (gender != null) {
            this.gender = gender;
        }
        this.updatedAt = Instant.now();
    }

    public void assignAddress(UUID addressId) {
        Objects.requireNonNull(addressId, "addressId must not be null");
        this.addressId = addressId;
        this.updatedAt = Instant.now();
    }

    public void removeAddress() {
        this.addressId = null;
        this.updatedAt = Instant.now();
    }

    public void linkKeycloakUser(UUID keycloakUserId) {
        Objects.requireNonNull(keycloakUserId, "keycloakUserId must not be null");
        this.keycloakUserId = keycloakUserId;
        this.updatedAt = Instant.now();
    }

    public void activate() {
        this.active = true;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public PersonType getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public UUID getAddressId() {
        return addressId;
    }

    public UUID getKeycloakUserId() {
        return keycloakUserId;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getFullName() {
        return fullName;
    }

    public String getTaxId() {
        return taxId;
    }

    public String getMotherName() {
        return motherName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Gender getGender() {
        return gender;
    }
}
