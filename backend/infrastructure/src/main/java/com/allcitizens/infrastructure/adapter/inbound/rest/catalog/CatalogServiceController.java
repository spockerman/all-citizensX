package com.allcitizens.infrastructure.adapter.inbound.rest.catalog;

import com.allcitizens.application.catalog.usecase.CreateCatalogServiceUseCase;
import com.allcitizens.application.catalog.usecase.DeleteCatalogServiceUseCase;
import com.allcitizens.application.catalog.usecase.GetCatalogServiceUseCase;
import com.allcitizens.application.catalog.usecase.ListCatalogServicesUseCase;
import com.allcitizens.application.catalog.usecase.UpdateCatalogServiceUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.catalog.dto.CatalogServiceResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.catalog.dto.CreateCatalogServiceRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.catalog.dto.UpdateCatalogServiceRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.catalog.mapper.CatalogServiceRestMapper;
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
@RequestMapping("/api/v1/catalog-services")
public class CatalogServiceController {

    private final CreateCatalogServiceUseCase createCatalogServiceUseCase;
    private final GetCatalogServiceUseCase getCatalogServiceUseCase;
    private final UpdateCatalogServiceUseCase updateCatalogServiceUseCase;
    private final ListCatalogServicesUseCase listCatalogServicesUseCase;
    private final DeleteCatalogServiceUseCase deleteCatalogServiceUseCase;
    private final CatalogServiceRestMapper mapper;

    public CatalogServiceController(CreateCatalogServiceUseCase createCatalogServiceUseCase,
                                    GetCatalogServiceUseCase getCatalogServiceUseCase,
                                    UpdateCatalogServiceUseCase updateCatalogServiceUseCase,
                                    ListCatalogServicesUseCase listCatalogServicesUseCase,
                                    DeleteCatalogServiceUseCase deleteCatalogServiceUseCase,
                                    CatalogServiceRestMapper mapper) {
        this.createCatalogServiceUseCase = createCatalogServiceUseCase;
        this.getCatalogServiceUseCase = getCatalogServiceUseCase;
        this.updateCatalogServiceUseCase = updateCatalogServiceUseCase;
        this.listCatalogServicesUseCase = listCatalogServicesUseCase;
        this.deleteCatalogServiceUseCase = deleteCatalogServiceUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<CatalogServiceResponse> create(@Valid @RequestBody CreateCatalogServiceRequest request) {
        var command = mapper.toCommand(request);
        var result = createCatalogServiceUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping
    public ResponseEntity<List<CatalogServiceResponse>> list(@RequestParam UUID tenantId) {
        var results = listCatalogServicesUseCase.execute(tenantId);
        var responses = results.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogServiceResponse> getById(@PathVariable UUID id) {
        var result = getCatalogServiceUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatalogServiceResponse> update(@PathVariable UUID id,
                                                         @RequestBody UpdateCatalogServiceRequest request) {
        var command = mapper.toCommand(request);
        var result = updateCatalogServiceUseCase.execute(id, command);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteCatalogServiceUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
