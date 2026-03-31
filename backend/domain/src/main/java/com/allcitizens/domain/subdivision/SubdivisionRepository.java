package com.allcitizens.domain.subdivision;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubdivisionRepository {
    Subdivision save(Subdivision subdivision);
    Optional<Subdivision> findById(UUID id);
    List<Subdivision> findAll();
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
