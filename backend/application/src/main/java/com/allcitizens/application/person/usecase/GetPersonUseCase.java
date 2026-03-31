package com.allcitizens.application.person.usecase;

import com.allcitizens.application.person.result.PersonResult;

import java.util.UUID;

public interface GetPersonUseCase {

    PersonResult execute(UUID id);
}
