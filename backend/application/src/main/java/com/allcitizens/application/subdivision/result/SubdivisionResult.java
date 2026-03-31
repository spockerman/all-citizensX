package com.allcitizens.application.subdivision.result;

import com.allcitizens.domain.subdivision.Subdivision;

import java.util.UUID;

public record SubdivisionResult(UUID id, String name, boolean active) {

    public static SubdivisionResult fromDomain(Subdivision subdivision) {
        return new SubdivisionResult(
            subdivision.getId(),
            subdivision.getName(),
            subdivision.isActive()
        );
    }
}
