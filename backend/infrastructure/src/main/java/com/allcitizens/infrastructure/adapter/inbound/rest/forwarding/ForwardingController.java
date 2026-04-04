package com.allcitizens.infrastructure.adapter.inbound.rest.forwarding;

import com.allcitizens.application.forwarding.usecase.GetForwardingUseCase;
import com.allcitizens.application.forwarding.usecase.UpdateForwardingUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.ForwardingResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.UpdateForwardingRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.mapper.ForwardingRestMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/forwardings")
public class ForwardingController {

    private final GetForwardingUseCase getUseCase;
    private final UpdateForwardingUseCase updateUseCase;
    private final ForwardingRestMapper mapper;

    public ForwardingController(GetForwardingUseCase getUseCase,
                                UpdateForwardingUseCase updateUseCase,
                                ForwardingRestMapper mapper) {
        this.getUseCase = getUseCase;
        this.updateUseCase = updateUseCase;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ForwardingResponse> get(@PathVariable UUID id) {
        var result = getUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ForwardingResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateForwardingRequest request) {
        var result = updateUseCase.execute(id, mapper.toCommand(request));
        return ResponseEntity.ok(mapper.toResponse(result));
    }
}
