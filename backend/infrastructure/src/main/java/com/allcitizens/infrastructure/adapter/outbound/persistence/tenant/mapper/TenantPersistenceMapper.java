package com.allcitizens.infrastructure.adapter.outbound.persistence.tenant.mapper;

import com.allcitizens.domain.tenant.Tenant;
import com.allcitizens.infrastructure.adapter.outbound.persistence.tenant.entity.TenantJpaEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TenantPersistenceMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};

    public Tenant toDomain(TenantJpaEntity entity) {
        return Tenant.reconstitute(
            entity.getId(),
            entity.getName(),
            entity.getCode(),
            entity.isActive(),
            parseConfig(entity.getConfig()),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public TenantJpaEntity toEntity(Tenant tenant) {
        var entity = new TenantJpaEntity();
        entity.setId(tenant.getId());
        entity.setName(tenant.getName());
        entity.setCode(tenant.getCode());
        entity.setActive(tenant.isActive());
        entity.setConfig(serializeConfig(tenant.getConfig()));
        entity.setCreatedAt(tenant.getCreatedAt());
        entity.setUpdatedAt(tenant.getUpdatedAt());
        return entity;
    }

    private Map<String, Object> parseConfig(String json) {
        if (json == null || json.isBlank()) {
            return new HashMap<>();
        }
        try {
            return OBJECT_MAPPER.readValue(json, MAP_TYPE);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse tenant config JSON", e);
        }
    }

    private String serializeConfig(Map<String, Object> config) {
        if (config == null || config.isEmpty()) {
            return "{}";
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(config);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize tenant config to JSON", e);
        }
    }
}
