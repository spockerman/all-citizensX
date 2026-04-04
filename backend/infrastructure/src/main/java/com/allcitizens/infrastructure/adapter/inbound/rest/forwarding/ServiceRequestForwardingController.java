package com.allcitizens.infrastructure.adapter.inbound.rest.forwarding;

import com.allcitizens.application.forwarding.usecase.CreateForwardingUseCase;
import com.allcitizens.application.forwarding.usecase.ListForwardingsByRequestUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.CreateForwardingRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.ForwardingResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.mapper.ForwardingRestMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/service-requests/{requestId}/forwardings")
public class ServiceRequestForwardingController {

    private final CreateForwardingUseCase createUseCase;
    private final ListForwardingsByRequestUseCase listUseCase;
    private final ForwardingRestMapper mapper;

    public ServiceRequestForwardingController(CreateForwardingUseCase createUseCase,
                                              ListForwardingsByRequestUseCase listUseCase,
                                              ForwardingRestMapper mapper) {
        this.createUseCase = createUseCase;
        this.listUseCase = listUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ForwardingResponse> create(
            @PathVariable UUID requestId,
            @Valid @RequestBody CreateForwardingRequest request) {
        var result = createUseCase.execute(mapper.toCommand(requestId, request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping
    public ResponseEntity<List<ForwardingResponse>> list(@PathVariable UUID requestId) {
        var results = listUseCase.execute(requestId);
        var body = results.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(body);
    }
}
