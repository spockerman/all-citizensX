package com.allcitizens.application.notification.service;

import com.allcitizens.application.notification.command.CreateNotificationRuleCommand;
import com.allcitizens.application.notification.command.UpdateNotificationRuleCommand;
import com.allcitizens.application.notification.result.NotificationRuleResult;
import com.allcitizens.application.notification.usecase.CreateNotificationRuleUseCase;
import com.allcitizens.application.notification.usecase.GetNotificationRuleUseCase;
import com.allcitizens.application.notification.usecase.ListNotificationRulesUseCase;
import com.allcitizens.application.notification.usecase.UpdateNotificationRuleUseCase;
import com.allcitizens.domain.catalog.CatalogServiceRepository;
import com.allcitizens.domain.exception.EntityNotFoundException;
import com.allcitizens.domain.notification.NotificationChannel;
import com.allcitizens.domain.notification.NotificationRule;
import com.allcitizens.domain.notification.NotificationRuleRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public class NotificationRuleApplicationService
        implements CreateNotificationRuleUseCase, GetNotificationRuleUseCase, UpdateNotificationRuleUseCase, ListNotificationRulesUseCase {

    private final NotificationRuleRepository ruleRepository;
    private final CatalogServiceRepository catalogServiceRepository;

    public NotificationRuleApplicationService(
            NotificationRuleRepository ruleRepository, CatalogServiceRepository catalogServiceRepository) {
        this.ruleRepository = ruleRepository;
        this.catalogServiceRepository = catalogServiceRepository;
    }

    @Override
    @Transactional
    public NotificationRuleResult execute(CreateNotificationRuleCommand command) {
        catalogServiceRepository
                .findById(command.serviceId())
                .orElseThrow(() -> new EntityNotFoundException("CatalogService", command.serviceId()));
        var channel = NotificationChannel.valueOf(command.channel().trim().toUpperCase());
        var rule = NotificationRule.create(command.serviceId(), command.event(), channel, command.template());
        rule = ruleRepository.save(rule);
        return NotificationRuleResult.fromDomain(rule);
    }

    @Override
    public NotificationRuleResult execute(UUID id) {
        var r = ruleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("NotificationRule", id));
        return NotificationRuleResult.fromDomain(r);
    }

    @Override
    @Transactional
    public NotificationRuleResult execute(UUID id, UpdateNotificationRuleCommand command) {
        var rule = ruleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("NotificationRule", id));
        NotificationChannel channel = null;
        if (command.channel() != null) {
            channel = NotificationChannel.valueOf(command.channel().trim().toUpperCase());
        }
        rule.update(command.event(), channel, command.template(), command.active());
        rule = ruleRepository.save(rule);
        return NotificationRuleResult.fromDomain(rule);
    }

    @Transactional
    public void deleteById(UUID id) {
        if (ruleRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("NotificationRule", id);
        }
        ruleRepository.deleteById(id);
    }

    @Override
    public List<NotificationRuleResult> listForService(UUID serviceId) {
        catalogServiceRepository
                .findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("CatalogService", serviceId));
        return ruleRepository.findByServiceId(serviceId).stream()
                .map(NotificationRuleResult::fromDomain)
                .toList();
    }
}
