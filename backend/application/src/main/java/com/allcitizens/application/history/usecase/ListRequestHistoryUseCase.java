package com.allcitizens.application.history.usecase;

import com.allcitizens.application.history.result.RequestHistoryResult;

import java.util.List;
import java.util.UUID;

public interface ListRequestHistoryUseCase {

    List<RequestHistoryResult> execute(UUID requestId);
}
