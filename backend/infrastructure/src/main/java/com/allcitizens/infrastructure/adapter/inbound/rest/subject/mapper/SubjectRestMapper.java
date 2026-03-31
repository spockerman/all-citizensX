package com.allcitizens.infrastructure.adapter.inbound.rest.subject.mapper;

import com.allcitizens.application.subject.command.CreateSubjectCommand;
import com.allcitizens.application.subject.command.UpdateSubjectCommand;
import com.allcitizens.application.subject.result.SubjectResult;
import com.allcitizens.infrastructure.adapter.inbound.rest.subject.dto.CreateSubjectRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.subject.dto.SubjectResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.subject.dto.UpdateSubjectRequest;
import org.springframework.stereotype.Component;

@Component
public class SubjectRestMapper {

    public CreateSubjectCommand toCommand(CreateSubjectRequest request) {
        return new CreateSubjectCommand(
            request.tenantId(),
            request.departmentId(),
            request.name(),
            request.visibleWeb(),
            request.visibleApp()
        );
    }

    public UpdateSubjectCommand toCommand(UpdateSubjectRequest request) {
        return new UpdateSubjectCommand(
            request.name(),
            request.departmentId(),
            request.active(),
            request.visibleWeb(),
            request.visibleApp()
        );
    }

    public SubjectResponse toResponse(SubjectResult result) {
        return new SubjectResponse(
            result.id(),
            result.tenantId(),
            result.departmentId(),
            result.name(),
            result.active(),
            result.visibleWeb(),
            result.visibleApp(),
            result.createdAt(),
            result.updatedAt()
        );
    }
}
