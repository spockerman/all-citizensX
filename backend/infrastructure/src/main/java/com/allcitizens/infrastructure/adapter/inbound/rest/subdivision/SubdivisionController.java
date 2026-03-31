package com.allcitizens.infrastructure.adapter.inbound.rest.subdivision;

import com.allcitizens.application.subdivision.usecase.CreateSubdivisionUseCase;
import com.allcitizens.application.subdivision.usecase.DeleteSubdivisionUseCase;
import com.allcitizens.application.subdivision.usecase.GetSubdivisionUseCase;
import com.allcitizens.application.subdivision.usecase.ListSubdivisionsUseCase;
import com.allcitizens.application.subdivision.usecase.UpdateSubdivisionUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.subdivision.dto.CreateSubdivisionRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.subdivision.dto.SubdivisionResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.subdivision.dto.UpdateSubdivisionRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.subdivision.mapper.SubdivisionRestMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subdivisions")
public class SubdivisionController {

    private final CreateSubdivisionUseCase createSubdivisionUseCase;
    private final GetSubdivisionUseCase getSubdivisionUseCase;
    private final UpdateSubdivisionUseCase updateSubdivisionUseCase;
    private final ListSubdivisionsUseCase listSubdivisionsUseCase;
    private final DeleteSubdivisionUseCase deleteSubdivisionUseCase;
    private final SubdivisionRestMapper mapper;

    public SubdivisionController(CreateSubdivisionUseCase createSubdivisionUseCase,
                                 GetSubdivisionUseCase getSubdivisionUseCase,
                                 UpdateSubdivisionUseCase updateSubdivisionUseCase,
                                 ListSubdivisionsUseCase listSubdivisionsUseCase,
                                 DeleteSubdivisionUseCase deleteSubdivisionUseCase,
                                 SubdivisionRestMapper mapper) {
        this.createSubdivisionUseCase = createSubdivisionUseCase;
        this.getSubdivisionUseCase = getSubdivisionUseCase;
        this.updateSubdivisionUseCase = updateSubdivisionUseCase;
        this.listSubdivisionsUseCase = listSubdivisionsUseCase;
        this.deleteSubdivisionUseCase = deleteSubdivisionUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<SubdivisionResponse> create(@Valid @RequestBody CreateSubdivisionRequest request) {
        var command = mapper.toCommand(request);
        var result = createSubdivisionUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping
    public ResponseEntity<List<SubdivisionResponse>> list() {
        var results = listSubdivisionsUseCase.execute();
        var responses = results.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubdivisionResponse> getById(@PathVariable UUID id) {
        var result = getSubdivisionUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubdivisionResponse> update(@PathVariable UUID id,
                                                     @RequestBody UpdateSubdivisionRequest request) {
        var command = mapper.toCommand(request);
        var result = updateSubdivisionUseCase.execute(id, command);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteSubdivisionUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
