package com.allcitizens.application.forwarding.usecase;

import com.allcitizens.application.forwarding.result.ForwardingAnswerResult;

import java.util.List;
import java.util.UUID;

public interface ListForwardingAnswersUseCase {

    List<ForwardingAnswerResult> execute(UUID forwardingId);
}
