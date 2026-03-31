package com.allcitizens.infrastructure.adapter.outbound.persistence.request.mapper;

import com.allcitizens.domain.request.Channel;
import com.allcitizens.domain.request.EmotionalState;
import com.allcitizens.domain.request.Priority;
import com.allcitizens.domain.request.RequestStatus;
import com.allcitizens.domain.request.ServiceRequest;
import com.allcitizens.infrastructure.adapter.outbound.persistence.request.entity.ServiceRequestJpaEntity;
import com.allcitizens.infrastructure.adapter.outbound.persistence.request.entity.ServiceRequestJpaEntity.ChannelJpa;
import com.allcitizens.infrastructure.adapter.outbound.persistence.request.entity.ServiceRequestJpaEntity.EmotionalStateJpa;
import com.allcitizens.infrastructure.adapter.outbound.persistence.request.entity.ServiceRequestJpaEntity.PriorityJpa;
import com.allcitizens.infrastructure.adapter.outbound.persistence.request.entity.ServiceRequestJpaEntity.RequestStatusJpa;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ServiceRequestPersistenceMapper {

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};
    private final ObjectMapper objectMapper;

    public ServiceRequestPersistenceMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ServiceRequest toDomain(ServiceRequestJpaEntity entity) {
        return ServiceRequest.reconstitute(
                entity.getId(),
                entity.getTenantId(),
                entity.getProtocol(),
                entity.getServiceId(),
                entity.getPersonId(),
                entity.getAddressId(),
                entity.getRequestTypeId(),
                entity.getIncidentId(),
                entity.getResponseMethodId(),
                entity.getChannel() != null ? Channel.valueOf(entity.getChannel().name()) : null,
                entity.getStatus() != null ? RequestStatus.valueOf(entity.getStatus().name()) : null,
                entity.getPriority() != null ? Priority.valueOf(entity.getPriority().name()) : null,
                entity.getDescription(),
                entity.getInternalNote(),
                parseJsonToMap(entity.getDynamicFields()),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.isConfidential(),
                entity.isAnonymous(),
                entity.getExternalDocType(),
                entity.getExternalDocNumber(),
                entity.getDueDate(),
                entity.getAgentUserId(),
                entity.getEmotionalState() != null
                        ? EmotionalState.valueOf(entity.getEmotionalState().name()) : null,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getClosedAt()
        );
    }

    public ServiceRequestJpaEntity toEntity(ServiceRequest domain) {
        var entity = new ServiceRequestJpaEntity();
        entity.setId(domain.getId());
        entity.setTenantId(domain.getTenantId());
        entity.setProtocol(domain.getProtocol());
        entity.setServiceId(domain.getServiceId());
        entity.setPersonId(domain.getPersonId());
        entity.setAddressId(domain.getAddressId());
        entity.setRequestTypeId(domain.getRequestTypeId());
        entity.setIncidentId(domain.getIncidentId());
        entity.setResponseMethodId(domain.getResponseMethodId());
        entity.setChannel(domain.getChannel() != null
                ? ChannelJpa.valueOf(domain.getChannel().name()) : null);
        entity.setStatus(domain.getStatus() != null
                ? RequestStatusJpa.valueOf(domain.getStatus().name()) : null);
        entity.setPriority(domain.getPriority() != null
                ? PriorityJpa.valueOf(domain.getPriority().name()) : null);
        entity.setDescription(domain.getDescription());
        entity.setInternalNote(domain.getInternalNote());
        entity.setDynamicFields(mapToJson(domain.getDynamicFields()));
        entity.setLatitude(domain.getLatitude());
        entity.setLongitude(domain.getLongitude());
        entity.setConfidential(domain.isConfidential());
        entity.setAnonymous(domain.isAnonymous());
        entity.setExternalDocType(domain.getExternalDocType());
        entity.setExternalDocNumber(domain.getExternalDocNumber());
        entity.setDueDate(domain.getDueDate());
        entity.setAgentUserId(domain.getAgentUserId());
        entity.setEmotionalState(domain.getEmotionalState() != null
                ? EmotionalStateJpa.valueOf(domain.getEmotionalState().name()) : null);
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setClosedAt(domain.getClosedAt());
        return entity;
    }

    private Map<String, Object> parseJsonToMap(String json) {
        if (json == null || json.isBlank()) {
            return Map.of();
        }
        try {
            return objectMapper.readValue(json, MAP_TYPE);
        } catch (JsonProcessingException e) {
            return Map.of();
        }
    }

    private String mapToJson(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return "{}";
        }
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
