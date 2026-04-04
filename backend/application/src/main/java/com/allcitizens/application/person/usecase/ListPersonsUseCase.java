package com.allcitizens.application.person.usecase;

import com.allcitizens.application.person.query.ListPersonsQuery;
import com.allcitizens.application.person.result.PersonResult;
import com.allcitizens.domain.common.PageResult;

public interface ListPersonsUseCase {

    PageResult<PersonResult> execute(ListPersonsQuery query);
}
