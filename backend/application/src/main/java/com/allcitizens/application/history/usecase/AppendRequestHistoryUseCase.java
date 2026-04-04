package com.allcitizens.application.history.usecase;

import com.allcitizens.application.history.command.AppendRequestHistoryCommand;
import com.allcitizens.application.history.result.RequestHistoryResult;

public interface AppendRequestHistoryUseCase {

    RequestHistoryResult execute(AppendRequestHistoryCommand command);
}
