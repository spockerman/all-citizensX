package com.allcitizens.application.forwarding.usecase;

import com.allcitizens.application.forwarding.result.ForwardingReasonResult;

import java.util.List;

public interface ListForwardingReasonsUseCase {

    List<ForwardingReasonResult> execute();
}
