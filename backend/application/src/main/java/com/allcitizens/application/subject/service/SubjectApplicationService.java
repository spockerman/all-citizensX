package com.allcitizens.application.subject.service;

import com.allcitizens.application.subject.command.CreateSubjectCommand;
import com.allcitizens.application.subject.command.UpdateSubjectCommand;
import com.allcitizens.application.subject.result.SubjectResult;
import com.allcitizens.domain.exception.EntityNotFoundException;
import com.allcitizens.domain.subject.Subject;
import com.allcitizens.domain.subject.SubjectRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public class SubjectApplicationService {

    private final SubjectRepository subjectRepository;

    public SubjectApplicationService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Transactional
    public SubjectResult create(CreateSubjectCommand command) {
        var subject = Subject.create(command.tenantId(), command.name());
        if (command.departmentId() != null) {
            subject.assignDepartment(command.departmentId());
        }
        subject.setVisibility(command.visibleWeb(), command.visibleApp());
        subject = subjectRepository.save(subject);
        return SubjectResult.fromDomain(subject);
    }

    public SubjectResult getById(UUID id) {
        var subject = subjectRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Subject", id));
        return SubjectResult.fromDomain(subject);
    }

    @Transactional
    public SubjectResult update(UUID id, UpdateSubjectCommand command) {
        var subject = subjectRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Subject", id));

        if (command.name() != null) {
            subject.update(command.name());
        }
        if (command.departmentId() != null) {
            subject.assignDepartment(command.departmentId());
        }
        if (command.active() != null) {
            if (command.active()) {
                subject.activate();
            } else {
                subject.deactivate();
            }
        }
        if (command.visibleWeb() != null || command.visibleApp() != null) {
            subject.setVisibility(
                command.visibleWeb() != null ? command.visibleWeb() : subject.isVisibleWeb(),
                command.visibleApp() != null ? command.visibleApp() : subject.isVisibleApp()
            );
        }

        subject = subjectRepository.save(subject);
        return SubjectResult.fromDomain(subject);
    }

    public List<SubjectResult> listByTenantId(UUID tenantId) {
        return subjectRepository.findAllByTenantId(tenantId).stream()
            .map(SubjectResult::fromDomain)
            .toList();
    }

    @Transactional
    public void delete(UUID id) {
        if (!subjectRepository.existsById(id)) {
            throw new EntityNotFoundException("Subject", id);
        }
        subjectRepository.deleteById(id);
    }
}
