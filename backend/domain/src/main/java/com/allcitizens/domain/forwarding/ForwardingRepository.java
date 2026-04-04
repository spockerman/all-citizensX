package com.allcitizens.domain.forwarding;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ForwardingRepository {

    Forwarding save(Forwarding forwarding);

    Optional<Forwarding> findById(UUID id);

    List<Forwarding> findAllByRequestId(UUID requestId);
}
