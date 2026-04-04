package com.allcitizens.application.forwarding.usecase;

import com.allcitizens.application.forwarding.command.AddForwardingAnswerCommand;
import com.allcitizens.application.forwarding.result.ForwardingAnswerResult;

public interface AddForwardingAnswerUseCase {

    ForwardingAnswerResult execute(AddForwardingAnswerCommand command);
}
