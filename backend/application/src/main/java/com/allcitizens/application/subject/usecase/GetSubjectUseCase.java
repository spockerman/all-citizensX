package com.allcitizens.application.subject.usecase;

import com.allcitizens.application.subject.result.SubjectResult;

import java.util.UUID;

public interface GetSubjectUseCase {

    SubjectResult execute(UUID id);
}
