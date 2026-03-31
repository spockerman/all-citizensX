package com.allcitizens.application.subject.usecase;

import com.allcitizens.application.subject.result.SubjectResult;

import java.util.List;
import java.util.UUID;

public interface ListSubjectsUseCase {

    List<SubjectResult> execute(UUID tenantId);
}
