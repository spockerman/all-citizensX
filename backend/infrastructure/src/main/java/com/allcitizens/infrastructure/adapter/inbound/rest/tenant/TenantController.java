package com.allcitizens.infrastructure.adapter.inbound.rest.tenant;

import com.allcitizens.application.tenant.usecase.CreateTenantUseCase;
import com.allcitizens.application.tenant.usecase.DeleteTenantUseCase;
import com.allcitizens.application.tenant.usecase.GetTenantUseCase;
import com.allcitizens.application.tenant.usecase.ListTenantsUseCase;
import com.allcitizens.application.tenant.usecase.UpdateTenantUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.tenant.dto.CreateTenantRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.tenant.dto.TenantResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.tenant.dto.UpdateTenantRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.tenant.mapper.TenantRestMapper;
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
@RequestMapping("/api/v1/tenants")
public class TenantController {

    private final CreateTenantUseCase createTenantUseCase;
    private final GetTenantUseCase getTenantUseCase;
    private final UpdateTenantUseCase updateTenantUseCase;
    private final ListTenantsUseCase listTenantsUseCase;
    private final DeleteTenantUseCase deleteTenantUseCase;
    private final TenantRestMapper mapper;

    public TenantController(CreateTenantUseCase createTenantUseCase,
                            GetTenantUseCase getTenantUseCase,
                            UpdateTenantUseCase updateTenantUseCase,
                            ListTenantsUseCase listTenantsUseCase,
                            DeleteTenantUseCase deleteTenantUseCase,
                            TenantRestMapper mapper) {
        this.createTenantUseCase = createTenantUseCase;
        this.getTenantUseCase = getTenantUseCase;
        this.updateTenantUseCase = updateTenantUseCase;
        this.listTenantsUseCase = listTenantsUseCase;
        this.deleteTenantUseCase = deleteTenantUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<TenantResponse> create(@Valid @RequestBody CreateTenantRequest request) {
        var command = mapper.toCommand(request);
        var result = createTenantUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping
    public ResponseEntity<List<TenantResponse>> list() {
        var results = listTenantsUseCase.execute();
        return ResponseEntity.ok(mapper.toResponseList(results));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantResponse> getById(@PathVariable UUID id) {
        var result = getTenantUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TenantResponse> update(@PathVariable UUID id,
                                                 @RequestBody UpdateTenantRequest request) {
        var command = mapper.toCommand(request);
        var result = updateTenantUseCase.execute(id, command);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteTenantUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
