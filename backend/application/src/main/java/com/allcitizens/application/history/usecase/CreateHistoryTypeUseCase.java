package com.allcitizens.application.history.usecase;

import com.allcitizens.application.history.command.CreateHistoryTypeCommand;
import com.allcitizens.application.history.result.HistoryTypeResult;

public interface CreateHistoryTypeUseCase {

    HistoryTypeResult execute(CreateHistoryTypeCommand command);
}
