package com.allcitizens.domain.forwarding;

import java.util.List;
import java.util.UUID;

public interface RedistributionRepository {

    Redistribution save(Redistribution redistribution);

    List<Redistribution> findAllByForwardingId(UUID forwardingId);
}
