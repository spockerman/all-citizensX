package com.allcitizens.domain.request;

import com.allcitizens.domain.exception.BusinessRuleException;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ServiceRequest {

    private UUID id;
    private UUID tenantId;
    private String protocol;
    private UUID serviceId;
    private UUID personId;
    private UUID addressId;
    private UUID requestTypeId;
    private UUID incidentId;
    private UUID responseMethodId;
    private Channel channel;
    private RequestStatus status;
    private Priority priority;
    private String description;
    private String internalNote;
    private Map<String, Object> dynamicFields;
    private Double latitude;
    private Double longitude;
    private boolean confidential;
    private boolean anonymous;
    private String externalDocType;
    private String externalDocNumber;
    private LocalDate dueDate;
    private UUID agentUserId;
    private EmotionalState emotionalState;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant closedAt;

    private ServiceRequest() {
    }

    public static ServiceRequest create(UUID tenantId, String protocol, UUID serviceId,
                                        Channel channel, Priority priority, String description) {
        Objects.requireNonNull(tenantId, "tenantId must not be null");
        Objects.requireNonNull(protocol, "protocol must not be null");
        Objects.requireNonNull(serviceId, "serviceId must not be null");

        var request = new ServiceRequest();
        request.id = UUID.randomUUID();
        request.tenantId = tenantId;
        request.protocol = protocol;
        request.serviceId = serviceId;
        request.channel = channel != null ? channel : Channel.PHONE;
        request.status = RequestStatus.OPEN;
        request.priority = priority != null ? priority : Priority.NORMAL;
        request.description = description;
        request.dynamicFields = Map.of();
        request.confidential = false;
        request.anonymous = false;
        request.createdAt = Instant.now();
        request.updatedAt = Instant.now();
        return request;
    }

    public static ServiceRequest reconstitute(UUID id, UUID tenantId, String protocol, UUID serviceId,
                                              UUID personId, UUID addressId, UUID requestTypeId,
                                              UUID incidentId, UUID responseMethodId, Channel channel,
                                              RequestStatus status, Priority priority, String description,
                                              String internalNote, Map<String, Object> dynamicFields,
                                              Double latitude, Double longitude, boolean confidential,
                                              boolean anonymous, String externalDocType,
                                              String externalDocNumber, LocalDate dueDate,
                                              UUID agentUserId, EmotionalState emotionalState,
                                              Instant createdAt, Instant updatedAt, Instant closedAt) {
        var request = new ServiceRequest();
        request.id = id;
        request.tenantId = tenantId;
        request.protocol = protocol;
        request.serviceId = serviceId;
        request.personId = personId;
        request.addressId = addressId;
        request.requestTypeId = requestTypeId;
        request.incidentId = incidentId;
        request.responseMethodId = responseMethodId;
        request.channel = channel;
        request.status = status;
        request.priority = priority;
        request.description = description;
        request.internalNote = internalNote;
        request.dynamicFields = dynamicFields != null ? dynamicFields : Map.of();
        request.latitude = latitude;
        request.longitude = longitude;
        request.confidential = confidential;
        request.anonymous = anonymous;
        request.externalDocType = externalDocType;
        request.externalDocNumber = externalDocNumber;
        request.dueDate = dueDate;
        request.agentUserId = agentUserId;
        request.emotionalState = emotionalState;
        request.createdAt = createdAt;
        request.updatedAt = updatedAt;
        request.closedAt = closedAt;
        return request;
    }

    public void updateDescription(String description) {
        this.description = description;
        this.updatedAt = Instant.now();
    }

    public void assignPerson(UUID personId) {
        this.personId = personId;
        this.updatedAt = Instant.now();
    }

    public void assignAddress(UUID addressId) {
        this.addressId = addressId;
        this.updatedAt = Instant.now();
    }

    public void setDynamicFields(Map<String, Object> fields) {
        this.dynamicFields = fields != null ? fields : Map.of();
        this.updatedAt = Instant.now();
    }

    public void setCoordinates(Double lat, Double lng) {
        this.latitude = lat;
        this.longitude = lng;
        this.updatedAt = Instant.now();
    }

    public void setConfidential(boolean confidential) {
        this.confidential = confidential;
        this.updatedAt = Instant.now();
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
        this.updatedAt = Instant.now();
    }

    public void forward() {
        if (status != RequestStatus.OPEN && status != RequestStatus.IN_PROGRESS) {
            throw new BusinessRuleException(
                    "Cannot forward a request in status " + status);
        }
        this.status = RequestStatus.FORWARDED;
        this.updatedAt = Instant.now();
    }

    public void answer() {
        this.status = RequestStatus.ANSWERED;
        this.updatedAt = Instant.now();
    }

    public void close() {
        this.status = RequestStatus.CLOSED;
        this.closedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void cancel() {
        this.status = RequestStatus.CANCELLED;
        this.closedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void reopen() {
        if (status != RequestStatus.CLOSED && status != RequestStatus.CANCELLED) {
            throw new BusinessRuleException(
                    "Cannot reopen a request in status " + status);
        }
        this.status = RequestStatus.REOPENED;
        this.closedAt = null;
        this.updatedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public UUID getTenantId() { return tenantId; }
    public String getProtocol() { return protocol; }
    public UUID getServiceId() { return serviceId; }
    public UUID getPersonId() { return personId; }
    public UUID getAddressId() { return addressId; }
    public UUID getRequestTypeId() { return requestTypeId; }
    public UUID getIncidentId() { return incidentId; }
    public UUID getResponseMethodId() { return responseMethodId; }
    public Channel getChannel() { return channel; }
    public RequestStatus getStatus() { return status; }
    public Priority getPriority() { return priority; }
    public String getDescription() { return description; }
    public String getInternalNote() { return internalNote; }
    public Map<String, Object> getDynamicFields() { return dynamicFields; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public boolean isConfidential() { return confidential; }
    public boolean isAnonymous() { return anonymous; }
    public String getExternalDocType() { return externalDocType; }
    public String getExternalDocNumber() { return externalDocNumber; }
    public LocalDate getDueDate() { return dueDate; }
    public UUID getAgentUserId() { return agentUserId; }
    public EmotionalState getEmotionalState() { return emotionalState; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Instant getClosedAt() { return closedAt; }
}
