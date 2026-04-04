package com.allcitizens.infrastructure.adapter.inbound.rest.person;

import com.allcitizens.application.person.query.ListPersonsQuery;
import com.allcitizens.application.person.usecase.CreatePersonUseCase;
import com.allcitizens.application.person.usecase.DeletePersonUseCase;
import com.allcitizens.application.person.usecase.GetPersonUseCase;
import com.allcitizens.application.person.usecase.ListPersonsUseCase;
import com.allcitizens.application.person.usecase.UpdatePersonUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.common.dto.PageResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.person.dto.CreatePersonRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.person.dto.PersonResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.person.dto.UpdatePersonRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.person.mapper.PersonRestMapper;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    private final CreatePersonUseCase createPersonUseCase;
    private final GetPersonUseCase getPersonUseCase;
    private final UpdatePersonUseCase updatePersonUseCase;
    private final ListPersonsUseCase listPersonsUseCase;
    private final DeletePersonUseCase deletePersonUseCase;
    private final PersonRestMapper mapper;

    public PersonController(CreatePersonUseCase createPersonUseCase,
                            GetPersonUseCase getPersonUseCase,
                            UpdatePersonUseCase updatePersonUseCase,
                            ListPersonsUseCase listPersonsUseCase,
                            DeletePersonUseCase deletePersonUseCase,
                            PersonRestMapper mapper) {
        this.createPersonUseCase = createPersonUseCase;
        this.getPersonUseCase = getPersonUseCase;
        this.updatePersonUseCase = updatePersonUseCase;
        this.listPersonsUseCase = listPersonsUseCase;
        this.deletePersonUseCase = deletePersonUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<PersonResponse> create(@Valid @RequestBody CreatePersonRequest request) {
        var command = mapper.toCommand(request);
        var result = createPersonUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping
    public ResponseEntity<PageResponse<PersonResponse>> list(
            @RequestParam UUID tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String q) {
        var safeSize = Math.min(Math.max(size, 1), 100);
        var query = new ListPersonsQuery(tenantId, page, safeSize, q);
        var results = listPersonsUseCase.execute(query);
        var responses = results.content().stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(new PageResponse<>(
                responses, results.totalElements(), results.totalPages(), results.page(), results.size()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> getById(@PathVariable UUID id) {
        var result = getPersonUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponse> update(@PathVariable UUID id,
                                                 @RequestBody UpdatePersonRequest request) {
        var command = mapper.toCommand(request);
        var result = updatePersonUseCase.execute(id, command);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deletePersonUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
