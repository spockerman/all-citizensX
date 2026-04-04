package com.allcitizens.domain.forwarding;

import java.util.List;
import java.util.UUID;

public interface ForwardingAnswerRepository {

    ForwardingAnswer save(ForwardingAnswer answer);

    List<ForwardingAnswer> findAllByForwardingId(UUID forwardingId);
}
