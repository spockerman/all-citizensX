package com.allcitizens.application.person.usecase;

import com.allcitizens.application.person.result.PersonResult;

import java.util.List;
import java.util.UUID;

public interface ListPersonsUseCase {

    List<PersonResult> execute(UUID tenantId);
}
