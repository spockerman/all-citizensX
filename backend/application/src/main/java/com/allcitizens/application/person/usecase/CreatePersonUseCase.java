package com.allcitizens.application.person.usecase;

import com.allcitizens.application.person.command.CreatePersonCommand;
import com.allcitizens.application.person.result.PersonResult;

public interface CreatePersonUseCase {

    PersonResult execute(CreatePersonCommand command);
}
