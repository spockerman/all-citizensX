package com.allcitizens.application.subject.usecase;

import com.allcitizens.application.subject.command.UpdateSubjectCommand;
import com.allcitizens.application.subject.result.SubjectResult;

import java.util.UUID;

public interface UpdateSubjectUseCase {

    SubjectResult execute(UUID id, UpdateSubjectCommand command);
}
