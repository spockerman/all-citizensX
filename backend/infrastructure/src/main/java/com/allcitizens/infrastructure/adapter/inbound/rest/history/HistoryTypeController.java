package com.allcitizens.infrastructure.adapter.inbound.rest.history;

import com.allcitizens.application.history.service.HistoryTypeApplicationService;
import com.allcitizens.infrastructure.adapter.inbound.rest.history.dto.CreateHistoryTypeRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.history.dto.HistoryTypeResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.history.mapper.HistoryRestMapper;
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
@RequestMapping("/api/v1/history-types")
public class HistoryTypeController {

    private final HistoryTypeApplicationService historyTypeService;
    private final HistoryRestMapper mapper;

    public HistoryTypeController(HistoryTypeApplicationService historyTypeService,
                                 HistoryRestMapper mapper) {
        this.historyTypeService = historyTypeService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<HistoryTypeResponse> create(@Valid @RequestBody CreateHistoryTypeRequest request) {
        var result = historyTypeService.execute(mapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping
    public ResponseEntity<List<HistoryTypeResponse>> list() {
        var results = historyTypeService.execute();
        var body = results.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(body);
    }
}
