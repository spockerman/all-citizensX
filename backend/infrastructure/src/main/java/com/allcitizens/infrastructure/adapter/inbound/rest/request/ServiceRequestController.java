package com.allcitizens.infrastructure.adapter.inbound.rest.request;

import com.allcitizens.application.request.query.ListServiceRequestsQuery;
import com.allcitizens.application.request.service.ServiceRequestApplicationService;
import com.allcitizens.infrastructure.adapter.inbound.rest.common.dto.PageResponse;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/service-requests")
public class ServiceRequestController {

    private final ServiceRequestApplicationService serviceRequestApplicationService;
    private final ServiceRequestRestMapper mapper;

    public ServiceRequestController(ServiceRequestApplicationService serviceRequestApplicationService,
                                    ServiceRequestRestMapper mapper) {
        this.serviceRequestApplicationService = serviceRequestApplicationService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ServiceRequestResponse> create(
            @Valid @RequestBody CreateServiceRequestRequest request) {
        var command = mapper.toCommand(request);
        var result = serviceRequestApplicationService.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ServiceRequestResponse>> list(
            @RequestParam UUID tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String q) {
        var safeSize = Math.min(Math.max(size, 1), 100);
        var query = new ListServiceRequestsQuery(tenantId, page, safeSize, q);
        var results = serviceRequestApplicationService.execute(query);
        var responses = results.content().stream().map(mapper::toResponse).toList();
        var body = new PageResponse<>(
                responses,
                results.totalElements(),
                results.totalPages(),
                results.page(),
                results.size()
        );
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequestResponse> get(@PathVariable UUID id) {
        var result = serviceRequestApplicationService.get(id);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceRequestResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateServiceRequestRequest request) {
        var command = mapper.toCommand(request);
        var result = serviceRequestApplicationService.execute(id, command);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<ServiceRequestResponse> close(@PathVariable UUID id) {
        var result = serviceRequestApplicationService.close(id);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ServiceRequestResponse> cancel(@PathVariable UUID id) {
        var result = serviceRequestApplicationService.cancel(id);
        return ResponseEntity.ok(mapper.toResponse(result));
    }
}
