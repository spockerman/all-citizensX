package com.allcitizens.application.person.usecase;

import com.allcitizens.application.person.command.UpdatePersonCommand;
import com.allcitizens.application.person.result.PersonResult;

import java.util.UUID;

public interface UpdatePersonUseCase {

    PersonResult execute(UUID id, UpdatePersonCommand command);
}
