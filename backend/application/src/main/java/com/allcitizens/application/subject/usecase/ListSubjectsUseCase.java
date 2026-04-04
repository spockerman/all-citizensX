package com.allcitizens.application.subject.usecase;

import com.allcitizens.application.subject.query.ListSubjectsQuery;
import com.allcitizens.application.subject.result.SubjectResult;
import com.allcitizens.domain.common.PageResult;

public interface ListSubjectsUseCase {

    PageResult<SubjectResult> execute(ListSubjectsQuery query);
}
