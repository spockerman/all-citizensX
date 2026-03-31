package com.allcitizens.infrastructure.adapter.outbound.persistence.request.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "service_request")
public class ServiceRequestJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "protocol", nullable = false, length = 30)
    private String protocol;

    @Column(name = "service_id", nullable = false)
    private UUID serviceId;

    @Column(name = "person_id")
    private UUID personId;

    @Column(name = "address_id")
    private UUID addressId;

    @Column(name = "request_type_id")
    private UUID requestTypeId;

    @Column(name = "incident_id")
    private UUID incidentId;

    @Column(name = "response_method_id")
    private UUID responseMethodId;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    private ChannelJpa channel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatusJpa status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private PriorityJpa priority;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "internal_note", columnDefinition = "TEXT")
    private String internalNote;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "dynamic_fields", columnDefinition = "jsonb")
    private String dynamicFields;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "confidential", nullable = false)
    private boolean confidential;

    @Column(name = "anonymous", nullable = false)
    private boolean anonymous;

    @Column(name = "external_doc_type", length = 60)
    private String externalDocType;

    @Column(name = "external_doc_number", length = 60)
    private String externalDocNumber;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "agent_user_id")
    private UUID agentUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "emotional_state")
    private EmotionalStateJpa emotionalState;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "closed_at")
    private Instant closedAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }
    public UUID getServiceId() { return serviceId; }
    public void setServiceId(UUID serviceId) { this.serviceId = serviceId; }
    public UUID getPersonId() { return personId; }
    public void setPersonId(UUID personId) { this.personId = personId; }
    public UUID getAddressId() { return addressId; }
    public void setAddressId(UUID addressId) { this.addressId = addressId; }
    public UUID getRequestTypeId() { return requestTypeId; }
    public void setRequestTypeId(UUID requestTypeId) { this.requestTypeId = requestTypeId; }
    public UUID getIncidentId() { return incidentId; }
    public void setIncidentId(UUID incidentId) { this.incidentId = incidentId; }
    public UUID getResponseMethodId() { return responseMethodId; }
    public void setResponseMethodId(UUID responseMethodId) { this.responseMethodId = responseMethodId; }
    public ChannelJpa getChannel() { return channel; }
    public void setChannel(ChannelJpa channel) { this.channel = channel; }
    public RequestStatusJpa getStatus() { return status; }
    public void setStatus(RequestStatusJpa status) { this.status = status; }
    public PriorityJpa getPriority() { return priority; }
    public void setPriority(PriorityJpa priority) { this.priority = priority; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getInternalNote() { return internalNote; }
    public void setInternalNote(String internalNote) { this.internalNote = internalNote; }
    public String getDynamicFields() { return dynamicFields; }
    public void setDynamicFields(String dynamicFields) { this.dynamicFields = dynamicFields; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public boolean isConfidential() { return confidential; }
    public void setConfidential(boolean confidential) { this.confidential = confidential; }
    public boolean isAnonymous() { return anonymous; }
    public void setAnonymous(boolean anonymous) { this.anonymous = anonymous; }
    public String getExternalDocType() { return externalDocType; }
    public void setExternalDocType(String externalDocType) { this.externalDocType = externalDocType; }
    public String getExternalDocNumber() { return externalDocNumber; }
    public void setExternalDocNumber(String externalDocNumber) { this.externalDocNumber = externalDocNumber; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public UUID getAgentUserId() { return agentUserId; }
    public void setAgentUserId(UUID agentUserId) { this.agentUserId = agentUserId; }
    public EmotionalStateJpa getEmotionalState() { return emotionalState; }
    public void setEmotionalState(EmotionalStateJpa emotionalState) { this.emotionalState = emotionalState; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public Instant getClosedAt() { return closedAt; }
    public void setClosedAt(Instant closedAt) { this.closedAt = closedAt; }

    public enum ChannelJpa {
        PHONE, WEB, MOBILE_APP, WHATSAPP, CHATBOT, IN_PERSON, EMAIL
    }

    public enum RequestStatusJpa {
        OPEN, IN_PROGRESS, FORWARDED, ANSWERED, CLOSED, CANCELLED, REOPENED
    }

    public enum PriorityJpa {
        LOW, NORMAL, HIGH, URGENT
    }

    public enum EmotionalStateJpa {
        CALM, ANXIOUS, IRRITATED, AGGRESSIVE, TEARFUL, INDIFFERENT
    }
}
