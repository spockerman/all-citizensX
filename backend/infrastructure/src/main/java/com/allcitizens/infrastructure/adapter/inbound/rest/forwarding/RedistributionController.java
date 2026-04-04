package com.allcitizens.infrastructure.adapter.inbound.rest.forwarding;

import com.allcitizens.application.forwarding.usecase.CreateRedistributionUseCase;
import com.allcitizens.application.forwarding.usecase.ListRedistributionsUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.CreateRedistributionRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.RedistributionResponse;
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
@RequestMapping("/api/v1/forwardings/{forwardingId}/redistributions")
public class RedistributionController {

    private final CreateRedistributionUseCase createUseCase;
    private final ListRedistributionsUseCase listUseCase;
    private final ForwardingRestMapper mapper;

    public RedistributionController(CreateRedistributionUseCase createUseCase,
                                    ListRedistributionsUseCase listUseCase,
                                    ForwardingRestMapper mapper) {
        this.createUseCase = createUseCase;
        this.listUseCase = listUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<RedistributionResponse> create(
            @PathVariable UUID forwardingId,
            @Valid @RequestBody CreateRedistributionRequest request) {
        var result = createUseCase.execute(mapper.toCommand(forwardingId, request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping
    public ResponseEntity<List<RedistributionResponse>> list(@PathVariable UUID forwardingId) {
        var results = listUseCase.execute(forwardingId);
        var body = results.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(body);
    }
}
