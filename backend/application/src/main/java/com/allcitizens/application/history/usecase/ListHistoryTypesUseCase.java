package com.allcitizens.application.history.usecase;

import com.allcitizens.application.history.result.HistoryTypeResult;

import java.util.List;

public interface ListHistoryTypesUseCase {

    List<HistoryTypeResult> execute();
}
