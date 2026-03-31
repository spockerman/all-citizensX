package com.allcitizens.application.subdivision.usecase;

import com.allcitizens.application.subdivision.result.SubdivisionResult;

import java.util.UUID;

public interface GetSubdivisionUseCase {

    SubdivisionResult execute(UUID id);
}
