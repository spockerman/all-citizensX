package com.allcitizens.infrastructure.adapter.inbound.rest.tenant;

import com.allcitizens.application.tenant.query.ListTenantsQuery;
import com.allcitizens.application.tenant.service.TenantApplicationService;
import com.allcitizens.infrastructure.adapter.inbound.rest.common.dto.PageResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantController {

    private final TenantApplicationService tenantApplicationService;
    private final TenantRestMapper mapper;

    public TenantController(TenantApplicationService tenantApplicationService,
                            TenantRestMapper mapper) {
        this.tenantApplicationService = tenantApplicationService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<TenantResponse> create(@Valid @RequestBody CreateTenantRequest request) {
        var command = mapper.toCommand(request);
        var result = tenantApplicationService.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping
    public ResponseEntity<PageResponse<TenantResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String q) {
        var safeSize = Math.min(Math.max(size, 1), 100);
        var query = new ListTenantsQuery(page, safeSize, q);
        var results = tenantApplicationService.execute(query);
        var responses = results.content().stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(new PageResponse<>(
                responses, results.totalElements(), results.totalPages(), results.page(), results.size()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantResponse> getById(@PathVariable UUID id) {
        var result = tenantApplicationService.execute(id);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TenantResponse> update(@PathVariable UUID id,
                                                 @RequestBody UpdateTenantRequest request) {
        var command = mapper.toCommand(request);
        var result = tenantApplicationService.execute(id, command);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        tenantApplicationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
