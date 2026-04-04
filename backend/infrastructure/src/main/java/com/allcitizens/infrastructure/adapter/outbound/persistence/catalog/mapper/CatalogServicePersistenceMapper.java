package com.allcitizens.infrastructure.adapter.outbound.persistence.catalog.mapper;

import com.allcitizens.domain.catalog.CatalogService;
import com.allcitizens.domain.request.Priority;
import com.allcitizens.infrastructure.adapter.outbound.persistence.catalog.entity.CatalogServiceJpaEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CatalogServicePersistenceMapper {

    private final ObjectMapper objectMapper;

    public CatalogServicePersistenceMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CatalogService toDomain(CatalogServiceJpaEntity entity) {
        return CatalogService.reconstitute(
            entity.getId(),
            entity.getTenantId(),
            entity.getSubjectId(),
            entity.getSubdivisionId(),
            entity.getDepartmentId(),
            entity.getDisplayName(),
            entity.getDescription(),
            entity.getSlaDays(),
            entity.getDefaultPriority() != null ? entity.getDefaultPriority() : Priority.NORMAL,
            entity.isAllowsAnonymous(),
            entity.isAllowsMultiForward(),
            entity.isVisibleWeb(),
            entity.isVisibleApp(),
            mapDynamicFieldsFromJson(entity.getDynamicFields()),
            entity.isActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public CatalogServiceJpaEntity toEntity(CatalogService domain) {
        var entity = new CatalogServiceJpaEntity();
        entity.setId(domain.getId());
        entity.setTenantId(domain.getTenantId());
        entity.setSubjectId(domain.getSubjectId());
        entity.setSubdivisionId(domain.getSubdivisionId());
        entity.setDepartmentId(domain.getDepartmentId());
        entity.setDisplayName(domain.getDisplayName());
        entity.setDescription(domain.getDescription());
        entity.setSlaDays(domain.getSlaDays());
        entity.setDefaultPriority(domain.getDefaultPriority());
        entity.setAllowsAnonymous(domain.isAllowsAnonymous());
        entity.setAllowsMultiForward(domain.isAllowsMultiForward());
        entity.setVisibleWeb(domain.isVisibleWeb());
        entity.setVisibleApp(domain.isVisibleApp());
        entity.setDynamicFields(mapDynamicFieldsToJson(domain.getDynamicFields()));
        entity.setActive(domain.isActive());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    private Map<String, Object> mapDynamicFieldsFromJson(String json) {
        if (json == null || json.isBlank()) {
            return new HashMap<>();
        }
        var trimmed = json.trim();
        if ("[]".equals(trimmed)) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to parse dynamic_fields JSON", e);
        }
    }

    private String mapDynamicFieldsToJson(Map<String, Object> dynamicFields) {
        if (dynamicFields == null || dynamicFields.isEmpty()) {
            return "[]";
        }
        try {
            return objectMapper.writeValueAsString(dynamicFields);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize dynamic_fields to JSON", e);
        }
    }
}
