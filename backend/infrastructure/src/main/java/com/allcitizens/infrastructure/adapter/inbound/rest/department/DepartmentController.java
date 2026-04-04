package com.allcitizens.infrastructure.adapter.inbound.rest.department;

import com.allcitizens.application.department.query.ListDepartmentsQuery;
import com.allcitizens.application.department.usecase.CreateDepartmentUseCase;
import com.allcitizens.application.department.usecase.DeleteDepartmentUseCase;
import com.allcitizens.application.department.usecase.GetDepartmentUseCase;
import com.allcitizens.application.department.usecase.ListDepartmentsUseCase;
import com.allcitizens.application.department.usecase.UpdateDepartmentUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.common.dto.PageResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.department.dto.CreateDepartmentRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.department.dto.DepartmentResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.department.dto.UpdateDepartmentRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.department.mapper.DepartmentRestMapper;
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
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final CreateDepartmentUseCase createDepartmentUseCase;
    private final GetDepartmentUseCase getDepartmentUseCase;
    private final UpdateDepartmentUseCase updateDepartmentUseCase;
    private final ListDepartmentsUseCase listDepartmentsUseCase;
    private final DeleteDepartmentUseCase deleteDepartmentUseCase;
    private final DepartmentRestMapper mapper;

    public DepartmentController(CreateDepartmentUseCase createDepartmentUseCase,
                                GetDepartmentUseCase getDepartmentUseCase,
                                UpdateDepartmentUseCase updateDepartmentUseCase,
                                ListDepartmentsUseCase listDepartmentsUseCase,
                                DeleteDepartmentUseCase deleteDepartmentUseCase,
                                DepartmentRestMapper mapper) {
        this.createDepartmentUseCase = createDepartmentUseCase;
        this.getDepartmentUseCase = getDepartmentUseCase;
        this.updateDepartmentUseCase = updateDepartmentUseCase;
        this.listDepartmentsUseCase = listDepartmentsUseCase;
        this.deleteDepartmentUseCase = deleteDepartmentUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<DepartmentResponse> create(@Valid @RequestBody CreateDepartmentRequest request) {
        var command = mapper.toCommand(request);
        var result = createDepartmentUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping
    public ResponseEntity<PageResponse<DepartmentResponse>> list(
            @RequestParam UUID tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String q) {
        var safeSize = Math.min(Math.max(size, 1), 100);
        var query = new ListDepartmentsQuery(tenantId, page, safeSize, q);
        var results = listDepartmentsUseCase.execute(query);
        var responses = results.content().stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(new PageResponse<>(
                responses, results.totalElements(), results.totalPages(), results.page(), results.size()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getById(@PathVariable UUID id) {
        var result = getDepartmentUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> update(@PathVariable UUID id,
                                                     @RequestBody UpdateDepartmentRequest request) {
        var command = mapper.toCommand(request);
        var result = updateDepartmentUseCase.execute(id, command);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteDepartmentUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
