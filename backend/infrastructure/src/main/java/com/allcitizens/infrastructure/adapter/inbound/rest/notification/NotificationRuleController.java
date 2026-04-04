package com.allcitizens.infrastructure.adapter.inbound.rest.notification;

import com.allcitizens.application.notification.usecase.CreateNotificationRuleUseCase;
import com.allcitizens.application.notification.usecase.DeleteNotificationRuleUseCase;
import com.allcitizens.application.notification.usecase.GetNotificationRuleUseCase;
import com.allcitizens.application.notification.usecase.ListNotificationRulesUseCase;
import com.allcitizens.application.notification.usecase.UpdateNotificationRuleUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.notification.dto.CreateNotificationRuleRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.notification.dto.NotificationRuleResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.notification.dto.UpdateNotificationRuleRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.notification.mapper.NotificationRuleRestMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notification-rules")
public class NotificationRuleController {

    private final CreateNotificationRuleUseCase createNotificationRuleUseCase;
    private final GetNotificationRuleUseCase getNotificationRuleUseCase;
    private final UpdateNotificationRuleUseCase updateNotificationRuleUseCase;
    private final DeleteNotificationRuleUseCase deleteNotificationRuleUseCase;
    private final ListNotificationRulesUseCase listNotificationRulesUseCase;
    private final NotificationRuleRestMapper mapper;

    public NotificationRuleController(
            CreateNotificationRuleUseCase createNotificationRuleUseCase,
            GetNotificationRuleUseCase getNotificationRuleUseCase,
            UpdateNotificationRuleUseCase updateNotificationRuleUseCase,
            DeleteNotificationRuleUseCase deleteNotificationRuleUseCase,
            ListNotificationRulesUseCase listNotificationRulesUseCase,
            NotificationRuleRestMapper mapper) {
        this.createNotificationRuleUseCase = createNotificationRuleUseCase;
        this.getNotificationRuleUseCase = getNotificationRuleUseCase;
        this.updateNotificationRuleUseCase = updateNotificationRuleUseCase;
        this.deleteNotificationRuleUseCase = deleteNotificationRuleUseCase;
        this.listNotificationRulesUseCase = listNotificationRulesUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<NotificationRuleResponse> create(
            @Valid @RequestBody CreateNotificationRuleRequest request) {
        var result = createNotificationRuleUseCase.execute(mapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationRuleResponse> getById(@PathVariable UUID id) {
        var result = getNotificationRuleUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @GetMapping
    public ResponseEntity<List<NotificationRuleResponse>> listByService(@RequestParam UUID serviceId) {
        var results = listNotificationRulesUseCase.listForService(serviceId);
        var body = results.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationRuleResponse> update(
            @PathVariable UUID id, @RequestBody UpdateNotificationRuleRequest request) {
        var result = updateNotificationRuleUseCase.execute(id, mapper.toCommand(request));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteNotificationRuleUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
