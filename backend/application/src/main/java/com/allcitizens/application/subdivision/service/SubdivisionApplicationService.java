package com.allcitizens.application.subdivision.service;

import com.allcitizens.application.subdivision.command.CreateSubdivisionCommand;
import com.allcitizens.application.subdivision.command.UpdateSubdivisionCommand;
import com.allcitizens.application.subdivision.query.ListSubdivisionsQuery;
import com.allcitizens.application.subdivision.result.SubdivisionResult;
import com.allcitizens.application.subdivision.usecase.ListSubdivisionsUseCase;
import com.allcitizens.domain.common.PageResult;
import com.allcitizens.domain.exception.EntityNotFoundException;
import com.allcitizens.domain.subdivision.Subdivision;
import com.allcitizens.domain.subdivision.SubdivisionRepository;
import jakarta.transaction.Transactional;

import java.util.UUID;

public class SubdivisionApplicationService implements ListSubdivisionsUseCase {

    private final SubdivisionRepository subdivisionRepository;

    public SubdivisionApplicationService(SubdivisionRepository subdivisionRepository) {
        this.subdivisionRepository = subdivisionRepository;
    }

    @Transactional
    public SubdivisionResult create(CreateSubdivisionCommand command) {
        var subdivision = Subdivision.create(command.name());
        subdivision = subdivisionRepository.save(subdivision);
        return SubdivisionResult.fromDomain(subdivision);
    }

    public SubdivisionResult getById(UUID id) {
        var subdivision = subdivisionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Subdivision", id));
        return SubdivisionResult.fromDomain(subdivision);
    }

    @Transactional
    public SubdivisionResult update(UUID id, UpdateSubdivisionCommand command) {
        var subdivision = subdivisionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Subdivision", id));

        if (command.name() != null) {
            subdivision.update(command.name());
        }
        if (command.active() != null) {
            if (command.active()) {
                subdivision.activate();
            } else {
                subdivision.deactivate();
            }
        }

        subdivision = subdivisionRepository.save(subdivision);
        return SubdivisionResult.fromDomain(subdivision);
    }

    @Override
    public PageResult<SubdivisionResult> execute(ListSubdivisionsQuery query) {
        var page = (query.search() == null || query.search().isBlank())
                ? subdivisionRepository.findAllPaged(query.page(), query.size())
                : subdivisionRepository.searchPaged(query.search().trim(), query.page(), query.size());
        var content = page.content().stream().map(SubdivisionResult::fromDomain).toList();
        return new PageResult<>(content, page.totalElements(), page.page(), page.size());
    }

    @Transactional
    public void delete(UUID id) {
        if (!subdivisionRepository.existsById(id)) {
            throw new EntityNotFoundException("Subdivision", id);
        }
        subdivisionRepository.deleteById(id);
    }
}
