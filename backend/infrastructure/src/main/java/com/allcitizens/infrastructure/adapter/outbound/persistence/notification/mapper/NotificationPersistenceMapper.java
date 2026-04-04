package com.allcitizens.infrastructure.adapter.outbound.persistence.notification.mapper;

import com.allcitizens.domain.notification.Notification;
import com.allcitizens.domain.notification.NotificationChannel;
import com.allcitizens.domain.notification.NotificationStatus;
import com.allcitizens.infrastructure.adapter.outbound.persistence.notification.entity.NotificationChannelJpa;
import com.allcitizens.infrastructure.adapter.outbound.persistence.notification.entity.NotificationJpaEntity;
import com.allcitizens.infrastructure.adapter.outbound.persistence.notification.entity.NotificationStatusJpa;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationPersistenceMapper {

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};
    private final ObjectMapper objectMapper;

    public NotificationPersistenceMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Notification toDomain(NotificationJpaEntity entity) {
        return Notification.reconstitute(
                entity.getId(),
                entity.getTenantId(),
                entity.getRequestId(),
                entity.getRecipientId(),
                entity.getChannel() != null ? NotificationChannel.valueOf(entity.getChannel().name()) : null,
                entity.getTitle(),
                entity.getMessage(),
                entity.getStatus() != null ? NotificationStatus.valueOf(entity.getStatus().name()) : null,
                parseJsonToMap(entity.getExtraData()),
                entity.getCreatedAt(),
                entity.getSentAt(),
                entity.getReadAt());
    }

    public NotificationJpaEntity toEntity(Notification domain) {
        var entity = new NotificationJpaEntity();
        entity.setId(domain.getId());
        entity.setTenantId(domain.getTenantId());
        entity.setRequestId(domain.getRequestId());
        entity.setRecipientId(domain.getRecipientId());
        entity.setChannel(domain.getChannel() != null
                ? NotificationChannelJpa.valueOf(domain.getChannel().name()) : null);
        entity.setTitle(domain.getTitle());
        entity.setMessage(domain.getMessage());
        entity.setStatus(domain.getStatus() != null
                ? NotificationStatusJpa.valueOf(domain.getStatus().name()) : null);
        entity.setExtraData(mapToJson(domain.getExtraData()));
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setSentAt(domain.getSentAt());
        entity.setReadAt(domain.getReadAt());
        return entity;
    }

    private Map<String, Object> parseJsonToMap(String json) {
        if (json == null || json.isBlank()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(json, MAP_TYPE);
        } catch (JsonProcessingException e) {
            return new HashMap<>();
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
