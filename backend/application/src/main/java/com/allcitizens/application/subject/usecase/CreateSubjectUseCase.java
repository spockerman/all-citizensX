package com.allcitizens.application.subject.usecase;

import com.allcitizens.application.subject.command.CreateSubjectCommand;
import com.allcitizens.application.subject.result.SubjectResult;

public interface CreateSubjectUseCase {

    SubjectResult execute(CreateSubjectCommand command);
}
