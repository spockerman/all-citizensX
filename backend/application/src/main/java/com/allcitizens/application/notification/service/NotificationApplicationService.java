package com.allcitizens.application.notification.service;

import com.allcitizens.application.notification.command.CreateNotificationCommand;
import com.allcitizens.application.notification.command.UpdateNotificationStatusCommand;
import com.allcitizens.application.notification.query.ListNotificationsQuery;
import com.allcitizens.application.notification.result.NotificationResult;
import com.allcitizens.application.notification.usecase.CreateNotificationUseCase;
import com.allcitizens.application.notification.usecase.GetNotificationUseCase;
import com.allcitizens.application.notification.usecase.ListNotificationsUseCase;
import com.allcitizens.application.notification.usecase.UpdateNotificationStatusUseCase;
import com.allcitizens.domain.exception.BusinessRuleException;
import com.allcitizens.domain.exception.EntityNotFoundException;
import com.allcitizens.domain.notification.Notification;
import com.allcitizens.domain.notification.NotificationChannel;
import com.allcitizens.domain.notification.NotificationRepository;
import com.allcitizens.domain.notification.NotificationStatus;
import com.allcitizens.domain.request.ServiceRequestRepository;
import com.allcitizens.domain.tenant.TenantRepository;
import com.allcitizens.domain.common.PageResult;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.UUID;

public class NotificationApplicationService
        implements CreateNotificationUseCase,
                GetNotificationUseCase,
                ListNotificationsUseCase,
                UpdateNotificationStatusUseCase {

    private final NotificationRepository notificationRepository;
    private final TenantRepository tenantRepository;
    private final ServiceRequestRepository serviceRequestRepository;

    public NotificationApplicationService(
            NotificationRepository notificationRepository,
            TenantRepository tenantRepository,
            ServiceRequestRepository serviceRequestRepository) {
        this.notificationRepository = notificationRepository;
        this.tenantRepository = tenantRepository;
        this.serviceRequestRepository = serviceRequestRepository;
    }

    @Override
    @Transactional
    public NotificationResult execute(CreateNotificationCommand command) {
        tenantRepository
                .findById(command.tenantId())
                .orElseThrow(() -> new EntityNotFoundException("Tenant", command.tenantId()));
        if (command.requestId() != null) {
            var request = serviceRequestRepository
                    .findById(command.requestId())
                    .orElseThrow(() -> new EntityNotFoundException("ServiceRequest", command.requestId()));
            if (!request.getTenantId().equals(command.tenantId())) {
                throw new BusinessRuleException("request does not belong to tenant");
            }
        }
        var channel = NotificationChannel.valueOf(command.channel().trim().toUpperCase());
        var notification = Notification.create(
                command.tenantId(),
                command.requestId(),
                command.recipientId(),
                channel,
                command.title(),
                command.message(),
                command.extraData());
        notification = notificationRepository.save(notification);
        return NotificationResult.fromDomain(notification);
    }

    @Override
    public NotificationResult execute(UUID id) {
        var n = notificationRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification", id));
        return NotificationResult.fromDomain(n);
    }

    @Override
    public PageResult<NotificationResult> execute(ListNotificationsQuery query) {
        var safeSize = Math.min(Math.max(query.size(), 1), 100);
        PageResult<Notification> page;
        if (query.requestId() != null) {
            page = notificationRepository.findByTenantIdAndRequestId(
                    query.tenantId(), query.requestId(), query.page(), safeSize);
        } else {
            page = notificationRepository.findByTenantId(query.tenantId(), query.page(), safeSize);
        }
        var mapped = page.content().stream().map(NotificationResult::fromDomain).toList();
        return new PageResult<>(mapped, page.totalElements(), page.page(), page.size());
    }

    @Override
    @Transactional
    public NotificationResult execute(UUID id, UpdateNotificationStatusCommand command) {
        var notification = notificationRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification", id));
        var newStatus =
                NotificationStatus.valueOf(command.status().trim().toUpperCase());
        switch (newStatus) {
            case SENT -> notification.markSent(command.sentAt() != null ? command.sentAt() : Instant.now());
            case FAILED -> notification.markFailed();
            case READ -> notification.markRead(command.readAt() != null ? command.readAt() : Instant.now());
            case PENDING -> throw new BusinessRuleException("Cannot set status back to PENDING");
        }
        notification = notificationRepository.save(notification);
        return NotificationResult.fromDomain(notification);
    }
}
