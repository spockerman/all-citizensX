package com.allcitizens.infrastructure.adapter.inbound.rest.history.mapper;

import com.allcitizens.application.history.command.AppendRequestHistoryCommand;
import com.allcitizens.application.history.command.CreateHistoryTypeCommand;
import com.allcitizens.application.history.result.HistoryTypeResult;
import com.allcitizens.application.history.result.RequestHistoryResult;
import com.allcitizens.infrastructure.adapter.inbound.rest.history.dto.AppendRequestHistoryRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.history.dto.CreateHistoryTypeRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.history.dto.HistoryTypeResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.history.dto.RequestHistoryResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class HistoryRestMapper {

    public CreateHistoryTypeCommand toCommand(CreateHistoryTypeRequest r) {
        return new CreateHistoryTypeCommand(r.name());
    }

    public HistoryTypeResponse toResponse(HistoryTypeResult r) {
        return new HistoryTypeResponse(r.id(), r.name());
    }

    public AppendRequestHistoryCommand toCommand(UUID requestId, AppendRequestHistoryRequest r) {
        return new AppendRequestHistoryCommand(
                requestId,
                r.historyTypeId(),
                r.userId(),
                r.description(),
                r.previousData(),
                r.newData()
        );
    }

    public RequestHistoryResponse toResponse(RequestHistoryResult r) {
        return new RequestHistoryResponse(
                r.id(),
                r.requestId(),
                r.historyTypeId(),
                r.userId(),
                r.description(),
                r.previousData(),
                r.newData(),
                r.createdAt()
        );
    }
}
