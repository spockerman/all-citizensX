package com.allcitizens.infrastructure.adapter.inbound.rest.subject;

import com.allcitizens.application.subject.usecase.CreateSubjectUseCase;
import com.allcitizens.application.subject.usecase.DeleteSubjectUseCase;
import com.allcitizens.application.subject.usecase.GetSubjectUseCase;
import com.allcitizens.application.subject.usecase.ListSubjectsUseCase;
import com.allcitizens.application.subject.usecase.UpdateSubjectUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.subject.dto.CreateSubjectRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.subject.dto.SubjectResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.subject.dto.UpdateSubjectRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.subject.mapper.SubjectRestMapper;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subjects")
public class SubjectController {

    private final CreateSubjectUseCase createSubjectUseCase;
    private final GetSubjectUseCase getSubjectUseCase;
    private final UpdateSubjectUseCase updateSubjectUseCase;
    private final ListSubjectsUseCase listSubjectsUseCase;
    private final DeleteSubjectUseCase deleteSubjectUseCase;
    private final SubjectRestMapper mapper;

    public SubjectController(CreateSubjectUseCase createSubjectUseCase,
                             GetSubjectUseCase getSubjectUseCase,
                             UpdateSubjectUseCase updateSubjectUseCase,
                             ListSubjectsUseCase listSubjectsUseCase,
                             DeleteSubjectUseCase deleteSubjectUseCase,
                             SubjectRestMapper mapper) {
        this.createSubjectUseCase = createSubjectUseCase;
        this.getSubjectUseCase = getSubjectUseCase;
        this.updateSubjectUseCase = updateSubjectUseCase;
        this.listSubjectsUseCase = listSubjectsUseCase;
        this.deleteSubjectUseCase = deleteSubjectUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<SubjectResponse> create(@Valid @RequestBody CreateSubjectRequest request) {
        var command = mapper.toCommand(request);
        var result = createSubjectUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping
    public ResponseEntity<List<SubjectResponse>> list(@RequestParam UUID tenantId) {
        var results = listSubjectsUseCase.execute(tenantId);
        var responses = results.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponse> getById(@PathVariable UUID id) {
        var result = getSubjectUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectResponse> update(@PathVariable UUID id,
                                                  @RequestBody UpdateSubjectRequest request) {
        var command = mapper.toCommand(request);
        var result = updateSubjectUseCase.execute(id, command);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteSubjectUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
