package com.allcitizens.infrastructure.adapter.inbound.rest.request;

import com.allcitizens.application.request.usecase.CancelServiceRequestUseCase;
import com.allcitizens.application.request.usecase.CloseServiceRequestUseCase;
import com.allcitizens.application.request.usecase.CreateServiceRequestUseCase;
import com.allcitizens.application.request.usecase.GetServiceRequestUseCase;
import com.allcitizens.application.request.usecase.ListServiceRequestsUseCase;
import com.allcitizens.application.request.usecase.UpdateServiceRequestUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.request.dto.CreateServiceRequestRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.request.dto.ServiceRequestResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.request.dto.UpdateServiceRequestRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.request.mapper.ServiceRequestRestMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/v1/service-requests")
public class ServiceRequestController {

    private final CreateServiceRequestUseCase createUseCase;
    private final GetServiceRequestUseCase getUseCase;
    private final UpdateServiceRequestUseCase updateUseCase;
    private final ListServiceRequestsUseCase listUseCase;
    private final CloseServiceRequestUseCase closeUseCase;
    private final CancelServiceRequestUseCase cancelUseCase;
    private final ServiceRequestRestMapper mapper;

    public ServiceRequestController(CreateServiceRequestUseCase createUseCase,
                                    GetServiceRequestUseCase getUseCase,
                                    UpdateServiceRequestUseCase updateUseCase,
                                    ListServiceRequestsUseCase listUseCase,
                                    CloseServiceRequestUseCase closeUseCase,
                                    CancelServiceRequestUseCase cancelUseCase,
                                    ServiceRequestRestMapper mapper) {
        this.createUseCase = createUseCase;
        this.getUseCase = getUseCase;
        this.updateUseCase = updateUseCase;
        this.listUseCase = listUseCase;
        this.closeUseCase = closeUseCase;
        this.cancelUseCase = cancelUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ServiceRequestResponse> create(
            @Valid @RequestBody CreateServiceRequestRequest request) {
        var command = mapper.toCommand(request);
        var result = createUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping
    public ResponseEntity<List<ServiceRequestResponse>> list(@RequestParam UUID tenantId) {
        var results = listUseCase.execute(tenantId);
        var responses = results.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequestResponse> get(@PathVariable UUID id) {
        var result = getUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceRequestResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateServiceRequestRequest request) {
        var command = mapper.toCommand(request);
        var result = updateUseCase.execute(id, command);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<ServiceRequestResponse> close(@PathVariable UUID id) {
        var result = closeUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ServiceRequestResponse> cancel(@PathVariable UUID id) {
        var result = cancelUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(result));
    }
}
