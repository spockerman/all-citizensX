package com.allcitizens.infrastructure.adapter.inbound.rest.history;

import com.allcitizens.application.history.service.RequestHistoryApplicationService;
import com.allcitizens.infrastructure.adapter.inbound.rest.history.dto.AppendRequestHistoryRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.history.dto.RequestHistoryResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.history.mapper.HistoryRestMapper;
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
@RequestMapping("/api/v1/service-requests/{requestId}/history")
public class ServiceRequestHistoryController {

    private final RequestHistoryApplicationService requestHistoryService;
    private final HistoryRestMapper mapper;

    public ServiceRequestHistoryController(RequestHistoryApplicationService requestHistoryService,
                                           HistoryRestMapper mapper) {
        this.requestHistoryService = requestHistoryService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<RequestHistoryResponse>> list(@PathVariable UUID requestId) {
        var results = requestHistoryService.execute(requestId);
        var body = results.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<RequestHistoryResponse> append(
            @PathVariable UUID requestId,
            @Valid @RequestBody AppendRequestHistoryRequest request) {
        var result = requestHistoryService.execute(mapper.toCommand(requestId, request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }
}
