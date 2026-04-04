package com.allcitizens.application.forwarding.service;

import com.allcitizens.application.forwarding.command.AddForwardingAnswerCommand;
import com.allcitizens.application.forwarding.command.CreateForwardingCommand;
import com.allcitizens.application.forwarding.command.CreateRedistributionCommand;
import com.allcitizens.application.forwarding.command.UpdateForwardingCommand;
import com.allcitizens.application.forwarding.result.ForwardingAnswerResult;
import com.allcitizens.application.forwarding.result.ForwardingResult;
import com.allcitizens.application.forwarding.result.RedistributionResult;
import com.allcitizens.domain.department.DepartmentRepository;
import com.allcitizens.domain.exception.EntityNotFoundException;
import com.allcitizens.domain.forwarding.Forwarding;
import com.allcitizens.domain.forwarding.ForwardingAnswer;
import com.allcitizens.domain.forwarding.ForwardingAnswerRepository;
import com.allcitizens.domain.forwarding.ForwardingRepository;
import com.allcitizens.domain.forwarding.ForwardingStatus;
import com.allcitizens.domain.forwarding.Redistribution;
import com.allcitizens.domain.forwarding.RedistributionRepository;
import com.allcitizens.domain.request.ServiceRequestRepository;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ForwardingApplicationService {

    private final ForwardingRepository forwardingRepository;
    private final ForwardingAnswerRepository answerRepository;
    private final RedistributionRepository redistributionRepository;
    private final ServiceRequestRepository serviceRequestRepository;
    private final DepartmentRepository departmentRepository;

    public ForwardingApplicationService(
            ForwardingRepository forwardingRepository,
            ForwardingAnswerRepository answerRepository,
            RedistributionRepository redistributionRepository,
            ServiceRequestRepository serviceRequestRepository,
            DepartmentRepository departmentRepository) {
        this.forwardingRepository = forwardingRepository;
        this.answerRepository = answerRepository;
        this.redistributionRepository = redistributionRepository;
        this.serviceRequestRepository = serviceRequestRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public ForwardingResult create(CreateForwardingCommand command) {
        var request = serviceRequestRepository.findById(command.requestId())
                .orElseThrow(() -> new EntityNotFoundException("ServiceRequest", command.requestId()));
        if (!departmentRepository.existsById(command.targetDepartmentId())) {
            throw new EntityNotFoundException("Department", command.targetDepartmentId());
        }
        if (command.sourceDepartmentId() != null
                && !departmentRepository.existsById(command.sourceDepartmentId())) {
            throw new EntityNotFoundException("Department", command.sourceDepartmentId());
        }

        request.forward();
        serviceRequestRepository.save(request);

        var forwarding = Forwarding.create(
                command.requestId(),
                command.targetDepartmentId(),
                command.sourceDepartmentId(),
                command.reasonId(),
                command.userId(),
                command.notes(),
                command.dueDate()
        );
        var saved = forwardingRepository.save(forwarding);
        return ForwardingResult.fromDomain(saved);
    }

    @Transactional
    public ForwardingResult getById(UUID id) {
        return ForwardingResult.fromDomain(findForwarding(id));
    }

    @Transactional
    public List<ForwardingResult> listByRequestId(UUID requestId) {
        if (serviceRequestRepository.findById(requestId).isEmpty()) {
            throw new EntityNotFoundException("ServiceRequest", requestId);
        }
        return forwardingRepository.findAllByRequestId(requestId).stream()
                .map(ForwardingResult::fromDomain)
                .toList();
    }

    @Transactional
    public ForwardingResult update(UUID id, UpdateForwardingCommand command) {
        var forwarding = findForwarding(id);
        if (command.status() != null) {
            forwarding.setStatus(command.status());
        }
        if (Boolean.TRUE.equals(command.read())) {
            forwarding.markRead(command.readAt() != null ? command.readAt() : Instant.now());
        }
        if (command.answeredAt() != null) {
            forwarding.setAnsweredAt(command.answeredAt());
        }
        var saved = forwardingRepository.save(forwarding);
        return ForwardingResult.fromDomain(saved);
    }

    @Transactional
    public ForwardingAnswerResult addAnswer(AddForwardingAnswerCommand command) {
        if (!departmentRepository.existsById(command.departmentId())) {
            throw new EntityNotFoundException("Department", command.departmentId());
        }
        var forwarding = findForwarding(command.forwardingId());
        var answer = ForwardingAnswer.create(
                command.forwardingId(),
                command.departmentId(),
                command.userId(),
                command.reasonId(),
                command.response()
        );
        var saved = answerRepository.save(answer);
        forwarding.setStatus(ForwardingStatus.ANSWERED);
        forwarding.setAnsweredAt(Instant.now());
        forwardingRepository.save(forwarding);
        return ForwardingAnswerResult.fromDomain(saved);
    }

    @Transactional
    public List<ForwardingAnswerResult> listAnswersByForwardingId(UUID forwardingId) {
        findForwarding(forwardingId);
        return answerRepository.findAllByForwardingId(forwardingId).stream()
                .map(ForwardingAnswerResult::fromDomain)
                .toList();
    }

    @Transactional
    public RedistributionResult createRedistribution(CreateRedistributionCommand command) {
        var forwarding = findForwarding(command.forwardingId());
        if (!departmentRepository.existsById(command.targetDepartmentId())) {
            throw new EntityNotFoundException("Department", command.targetDepartmentId());
        }
        var redistribution = Redistribution.create(
                command.forwardingId(),
                command.targetDepartmentId(),
                command.userId(),
                command.notes()
        );
        var saved = redistributionRepository.save(redistribution);
        forwarding.setStatus(ForwardingStatus.REDISTRIBUTED);
        forwardingRepository.save(forwarding);
        return RedistributionResult.fromDomain(saved);
    }

    @Transactional
    public List<RedistributionResult> listRedistributionsByForwardingId(UUID forwardingId) {
        findForwarding(forwardingId);
        return redistributionRepository.findAllByForwardingId(forwardingId).stream()
                .map(RedistributionResult::fromDomain)
                .toList();
    }

    private Forwarding findForwarding(UUID id) {
        return forwardingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Forwarding", id));
    }
}
