package com.allcitizens.infrastructure.adapter.inbound.rest.forwarding;

import com.allcitizens.application.forwarding.usecase.AddForwardingAnswerUseCase;
import com.allcitizens.application.forwarding.usecase.ListForwardingAnswersUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.AddForwardingAnswerRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.ForwardingAnswerResponse;
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
@RequestMapping("/api/v1/forwardings/{forwardingId}/responses")
public class ForwardingAnswerController {

    private final AddForwardingAnswerUseCase addUseCase;
    private final ListForwardingAnswersUseCase listUseCase;
    private final ForwardingRestMapper mapper;

    public ForwardingAnswerController(AddForwardingAnswerUseCase addUseCase,
                                      ListForwardingAnswersUseCase listUseCase,
                                      ForwardingRestMapper mapper) {
        this.addUseCase = addUseCase;
        this.listUseCase = listUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ForwardingAnswerResponse> create(
            @PathVariable UUID forwardingId,
            @Valid @RequestBody AddForwardingAnswerRequest request) {
        var result = addUseCase.execute(mapper.toCommand(forwardingId, request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping
    public ResponseEntity<List<ForwardingAnswerResponse>> list(@PathVariable UUID forwardingId) {
        var results = listUseCase.execute(forwardingId);
        var body = results.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(body);
    }
}
