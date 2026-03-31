package com.allcitizens.domain.exception;

import java.util.UUID;

public class EntityNotFoundException extends DomainException {

    public EntityNotFoundException(String entityName, UUID id) {
        super(entityName + " not found with id " + id);
    }

    public EntityNotFoundException(String entityName, String field, String value) {
        super(entityName + " not found with " + field + " = " + value);
    }
}
