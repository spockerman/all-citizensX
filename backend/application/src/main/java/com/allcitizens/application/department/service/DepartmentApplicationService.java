package com.allcitizens.application.department.service;

import com.allcitizens.application.department.command.CreateDepartmentCommand;
import com.allcitizens.application.department.command.UpdateDepartmentCommand;
import com.allcitizens.application.department.result.DepartmentResult;
import com.allcitizens.domain.department.Department;
import com.allcitizens.domain.department.DepartmentRepository;
import com.allcitizens.domain.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public class DepartmentApplicationService {

    private final DepartmentRepository departmentRepository;

    public DepartmentApplicationService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public DepartmentResult create(CreateDepartmentCommand command) {
        if (command.parentId() != null && !departmentRepository.existsById(command.parentId())) {
            throw new EntityNotFoundException("Department", command.parentId());
        }

        var department = Department.create(
            command.tenantId(),
            command.name(),
            command.abbreviation(),
            command.email(),
            command.canRespond(),
            command.isRoot(),
            command.displayOrder()
        );

        if (command.parentId() != null) {
            department.assignParent(command.parentId());
        }

        department = departmentRepository.save(department);
        return DepartmentResult.fromDomain(department);
    }

    public DepartmentResult getById(UUID id) {
        var department = departmentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Department", id));
        return DepartmentResult.fromDomain(department);
    }

    @Transactional
    public DepartmentResult update(UUID id, UpdateDepartmentCommand command) {
        var department = departmentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Department", id));

        department.update(
            command.name(),
            command.abbreviation(),
            command.email(),
            command.canRespond() != null ? command.canRespond() : department.isCanRespond(),
            command.displayOrder()
        );

        if (command.active() != null) {
            if (command.active()) {
                department.activate();
            } else {
                department.deactivate();
            }
        }

        if (command.parentId() != null) {
            department.assignParent(command.parentId());
        }

        department = departmentRepository.save(department);
        return DepartmentResult.fromDomain(department);
    }

    public List<DepartmentResult> listByTenantId(UUID tenantId) {
        return departmentRepository.findAllByTenantId(tenantId).stream()
            .map(DepartmentResult::fromDomain)
            .toList();
    }

    @Transactional
    public void delete(UUID id) {
        if (!departmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Department", id);
        }
        departmentRepository.deleteById(id);
    }
}
