package com.allcitizens.infrastructure.adapter.outbound.persistence.history.mapper;

import com.allcitizens.domain.history.RequestHistory;
import com.allcitizens.infrastructure.adapter.outbound.persistence.history.entity.RequestHistoryJpaEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RequestHistoryPersistenceMapper {

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper;

    public RequestHistoryPersistenceMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public RequestHistory toDomain(RequestHistoryJpaEntity e) {
        return RequestHistory.reconstitute(
                e.getId(),
                e.getRequestId(),
                e.getHistoryTypeId(),
                e.getUserId(),
                e.getDescription(),
                fromJson(e.getPreviousData()),
                fromJson(e.getNewData()),
                e.getCreatedAt()
        );
    }

    public RequestHistoryJpaEntity toEntity(RequestHistory d) {
        var e = new RequestHistoryJpaEntity();
        e.setId(d.getId());
        e.setRequestId(d.getRequestId());
        e.setHistoryTypeId(d.getHistoryTypeId());
        e.setUserId(d.getUserId());
        e.setDescription(d.getDescription());
        e.setPreviousData(toJson(d.getPreviousData()));
        e.setNewData(toJson(d.getNewData()));
        e.setCreatedAt(d.getCreatedAt());
        return e;
    }

    private Map<String, Object> fromJson(String json) {
        if (json == null || json.isBlank()) {
            return Map.of();
        }
        try {
            Map<String, Object> m = objectMapper.readValue(json, MAP_TYPE);
            return m != null ? m : Map.of();
        } catch (JsonProcessingException ex) {
            return Map.of();
        }
    }

    private String toJson(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot serialize history JSON", e);
        }
    }
}
