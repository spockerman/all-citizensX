package com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.mapper;

import com.allcitizens.application.forwarding.command.AddForwardingAnswerCommand;
import com.allcitizens.application.forwarding.command.CreateForwardingCommand;
import com.allcitizens.application.forwarding.command.CreateForwardingReasonCommand;
import com.allcitizens.application.forwarding.command.CreateRedistributionCommand;
import com.allcitizens.application.forwarding.command.UpdateForwardingCommand;
import com.allcitizens.application.forwarding.result.ForwardingAnswerResult;
import com.allcitizens.application.forwarding.result.ForwardingReasonResult;
import com.allcitizens.application.forwarding.result.ForwardingResult;
import com.allcitizens.application.forwarding.result.RedistributionResult;
import com.allcitizens.domain.forwarding.ForwardingStatus;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.AddForwardingAnswerRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.CreateForwardingReasonRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.CreateForwardingRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.CreateRedistributionRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.ForwardingAnswerResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.ForwardingReasonResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.ForwardingResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.RedistributionResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto.UpdateForwardingRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ForwardingRestMapper {

    public CreateForwardingReasonCommand toCommand(CreateForwardingReasonRequest r) {
        return new CreateForwardingReasonCommand(r.name(), r.type());
    }

    public ForwardingReasonResponse toResponse(ForwardingReasonResult r) {
        return new ForwardingReasonResponse(r.id(), r.name(), r.type());
    }

    public CreateForwardingCommand toCommand(UUID requestId, CreateForwardingRequest r) {
        return new CreateForwardingCommand(
                requestId,
                r.targetDepartmentId(),
                r.sourceDepartmentId(),
                r.reasonId(),
                r.userId(),
                r.notes(),
                r.dueDate()
        );
    }

    public ForwardingResponse toResponse(ForwardingResult r) {
        return new ForwardingResponse(
                r.id(),
                r.requestId(),
                r.targetDepartmentId(),
                r.sourceDepartmentId(),
                r.reasonId(),
                r.userId(),
                r.status().name(),
                r.notes(),
                r.dueDate(),
                r.read(),
                r.readAt(),
                r.createdAt(),
                r.updatedAt(),
                r.answeredAt()
        );
    }

    public UpdateForwardingCommand toCommand(UpdateForwardingRequest r) {
        ForwardingStatus status = r.status() != null && !r.status().isBlank()
                ? ForwardingStatus.valueOf(r.status())
                : null;
        return new UpdateForwardingCommand(status, r.read(), r.readAt(), r.answeredAt());
    }

    public AddForwardingAnswerCommand toCommand(UUID forwardingId, AddForwardingAnswerRequest r) {
        return new AddForwardingAnswerCommand(
                forwardingId,
                r.departmentId(),
                r.userId(),
                r.reasonId(),
                r.response()
        );
    }

    public ForwardingAnswerResponse toResponse(ForwardingAnswerResult r) {
        return new ForwardingAnswerResponse(
                r.id(),
                r.forwardingId(),
                r.departmentId(),
                r.userId(),
                r.reasonId(),
                r.response(),
                r.createdAt()
        );
    }

    public CreateRedistributionCommand toCommand(UUID forwardingId, CreateRedistributionRequest r) {
        return new CreateRedistributionCommand(forwardingId, r.targetDepartmentId(), r.userId(), r.notes());
    }

    public RedistributionResponse toResponse(RedistributionResult r) {
        return new RedistributionResponse(
                r.id(),
                r.forwardingId(),
                r.targetDepartmentId(),
                r.userId(),
                r.status().name(),
                r.read(),
                r.notes(),
                r.createdAt()
        );
    }
}
