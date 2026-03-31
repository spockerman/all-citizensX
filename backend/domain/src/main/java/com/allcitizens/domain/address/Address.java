package com.allcitizens.domain.address;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Address {

    private UUID id;
    private String zipCode;
    private String street;
    private String number;
    private String complement;
    private UUID districtId;
    private UUID cityId;
    private String stateCode;
    private String landmark;
    private Double latitude;
    private Double longitude;
    private Instant createdAt;
    private Instant updatedAt;

    private Address() {
    }

    public static Address create(UUID cityId, String stateCode, String zipCode, String street, String number,
                                 String complement, UUID districtId, String landmark, Double latitude,
                                 Double longitude) {
        Objects.requireNonNull(cityId, "cityId must not be null");
        if (stateCode == null || stateCode.isBlank()) {
            throw new IllegalArgumentException("stateCode must not be blank");
        }

        var address = new Address();
        address.id = UUID.randomUUID();
        address.cityId = cityId;
        address.stateCode = stateCode;
        address.zipCode = zipCode;
        address.street = street;
        address.number = number;
        address.complement = complement;
        address.districtId = districtId;
        address.landmark = landmark;
        address.latitude = latitude;
        address.longitude = longitude;
        address.createdAt = Instant.now();
        address.updatedAt = Instant.now();
        return address;
    }

    public static Address reconstitute(UUID id, String zipCode, String street, String number, String complement,
                                       UUID districtId, UUID cityId, String stateCode, String landmark,
                                       Double latitude, Double longitude, Instant createdAt, Instant updatedAt) {
        var address = new Address();
        address.id = id;
        address.zipCode = zipCode;
        address.street = street;
        address.number = number;
        address.complement = complement;
        address.districtId = districtId;
        address.cityId = cityId;
        address.stateCode = stateCode;
        address.landmark = landmark;
        address.latitude = latitude;
        address.longitude = longitude;
        address.createdAt = createdAt;
        address.updatedAt = updatedAt;
        return address;
    }

    public void update(String zipCode, String street, String number, String complement, UUID districtId,
                       String landmark) {
        this.zipCode = zipCode;
        this.street = street;
        this.number = number;
        this.complement = complement;
        this.districtId = districtId;
        this.landmark = landmark;
        this.updatedAt = Instant.now();
    }

    public void setCoordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.updatedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getZipCode() { return zipCode; }
    public String getStreet() { return street; }
    public String getNumber() { return number; }
    public String getComplement() { return complement; }
    public UUID getDistrictId() { return districtId; }
    public UUID getCityId() { return cityId; }
    public String getStateCode() { return stateCode; }
    public String getLandmark() { return landmark; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
