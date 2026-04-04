package com.allcitizens.infrastructure.adapter.inbound.rest.notification;

import com.allcitizens.application.notification.query.ListNotificationsQuery;
import com.allcitizens.application.notification.usecase.CreateNotificationUseCase;
import com.allcitizens.application.notification.usecase.GetNotificationUseCase;
import com.allcitizens.application.notification.usecase.ListNotificationsUseCase;
import com.allcitizens.application.notification.usecase.UpdateNotificationStatusUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.common.dto.PageResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.notification.dto.CreateNotificationRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.notification.dto.NotificationResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.notification.dto.UpdateNotificationStatusRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.notification.mapper.NotificationRestMapper;
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
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final CreateNotificationUseCase createNotificationUseCase;
    private final GetNotificationUseCase getNotificationUseCase;
    private final ListNotificationsUseCase listNotificationsUseCase;
    private final UpdateNotificationStatusUseCase updateNotificationStatusUseCase;
    private final NotificationRestMapper mapper;

    public NotificationController(
            CreateNotificationUseCase createNotificationUseCase,
            GetNotificationUseCase getNotificationUseCase,
            ListNotificationsUseCase listNotificationsUseCase,
            UpdateNotificationStatusUseCase updateNotificationStatusUseCase,
            NotificationRestMapper mapper) {
        this.createNotificationUseCase = createNotificationUseCase;
        this.getNotificationUseCase = getNotificationUseCase;
        this.listNotificationsUseCase = listNotificationsUseCase;
        this.updateNotificationStatusUseCase = updateNotificationStatusUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> create(@Valid @RequestBody CreateNotificationRequest request) {
        var result = createNotificationUseCase.execute(mapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getById(@PathVariable UUID id) {
        var result = getNotificationUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @GetMapping
    public ResponseEntity<PageResponse<NotificationResponse>> list(
            @RequestParam UUID tenantId,
            @RequestParam(required = false) UUID requestId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var safeSize = Math.min(Math.max(size, 1), 100);
        var query = new ListNotificationsQuery(tenantId, requestId, page, safeSize);
        var results = listNotificationsUseCase.execute(query);
        var content = results.content().stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(new PageResponse<>(
                content, results.totalElements(), results.totalPages(), results.page(), results.size()));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<NotificationResponse> updateStatus(
            @PathVariable UUID id, @Valid @RequestBody UpdateNotificationStatusRequest request) {
        var result = updateNotificationStatusUseCase.execute(id, mapper.toCommand(request));
        return ResponseEntity.ok(mapper.toResponse(result));
    }
}
