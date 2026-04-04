package com.allcitizens.infrastructure.adapter.inbound.rest.forwarding;

import com.allcitizens.application.forwarding.service.ForwardingReasonApplicationService;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.CreateForwardingReasonRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.ForwardingReasonResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.mapper.ForwardingRestMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/forwarding-reasons")
public class ForwardingReasonController {

    private final ForwardingReasonApplicationService forwardingReasonService;
    private final ForwardingRestMapper mapper;

    public ForwardingReasonController(ForwardingReasonApplicationService forwardingReasonService,
                                      ForwardingRestMapper mapper) {
        this.forwardingReasonService = forwardingReasonService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ForwardingReasonResponse> create(
            @Valid @RequestBody CreateForwardingReasonRequest request) {
        var result = forwardingReasonService.execute(mapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping
    public ResponseEntity<List<ForwardingReasonResponse>> list() {
        var results = forwardingReasonService.execute();
        var body = results.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(body);
    }
}
