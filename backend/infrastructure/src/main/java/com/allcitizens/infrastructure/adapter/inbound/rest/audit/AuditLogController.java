package com.allcitizens.infrastructure.adapter.inbound.rest.audit;

import com.allcitizens.application.audit.query.ListAuditLogsQuery;
import com.allcitizens.application.audit.result.AuditLogResult;
import com.allcitizens.application.audit.usecase.ListAuditLogsUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.audit.dto.AuditLogResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.audit.mapper.AuditLogRestMapper;
import com.allcitizens.infrastructure.adapter.inbound.rest.common.dto.PageResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/audit-logs")
public class AuditLogController {

    private final ListAuditLogsUseCase listAuditLogsUseCase;
    private final AuditLogRestMapper mapper;

    public AuditLogController(ListAuditLogsUseCase listAuditLogsUseCase, AuditLogRestMapper mapper) {
        this.listAuditLogsUseCase = listAuditLogsUseCase;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<PageResponse<AuditLogResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        var safeSize = Math.min(Math.max(size, 1), 100);
        var query = new ListAuditLogsQuery(page, safeSize, from, to);
        PageResponse<AuditLogResponse> body = toPageResponse(listAuditLogsUseCase.execute(query));
        return ResponseEntity.ok(body);
    }

    private PageResponse<AuditLogResponse> toPageResponse(
            com.allcitizens.domain.common.PageResult<AuditLogResult> results) {
        var content = results.content().stream().map(mapper::toResponse).toList();
        return new PageResponse<>(
                content, results.totalElements(), results.totalPages(), results.page(), results.size());
    }
}
