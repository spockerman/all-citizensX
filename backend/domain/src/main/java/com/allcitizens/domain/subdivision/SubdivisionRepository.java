package com.allcitizens.domain.subdivision;

import com.allcitizens.domain.common.PageResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubdivisionRepository {
    Subdivision save(Subdivision subdivision);
    Optional<Subdivision> findById(UUID id);
    List<Subdivision> findAll();

    PageResult<Subdivision> findAllPaged(int page, int size);

    PageResult<Subdivision> searchPaged(String query, int page, int size);
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
