package com.allcitizens.application.subdivision.usecase;

import com.allcitizens.application.subdivision.command.UpdateSubdivisionCommand;
import com.allcitizens.application.subdivision.result.SubdivisionResult;

import java.util.UUID;

public interface UpdateSubdivisionUseCase {

    SubdivisionResult execute(UUID id, UpdateSubdivisionCommand command);
}
