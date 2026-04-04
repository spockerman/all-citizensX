package com.allcitizens.domain.forwarding;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ForwardingReasonRepository {

    ForwardingReason save(ForwardingReason reason);

    Optional<ForwardingReason> findById(UUID id);

    List<ForwardingReason> findAll();
}
