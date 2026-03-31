package com.allcitizens.infrastructure.adapter.inbound.rest.request;

import com.allcitizens.application.request.command.CreateServiceRequestCommand;
import com.allcitizens.application.request.command.UpdateServiceRequestCommand;
import com.allcitizens.application.request.result.ServiceRequestResult;
import com.allcitizens.application.request.usecase.CancelServiceRequestUseCase;
import com.allcitizens.application.request.usecase.CloseServiceRequestUseCase;
import com.allcitizens.application.request.usecase.CreateServiceRequestUseCase;
import com.allcitizens.application.request.usecase.GetServiceRequestUseCase;
import com.allcitizens.application.request.usecase.ListServiceRequestsUseCase;
import com.allcitizens.application.request.usecase.UpdateServiceRequestUseCase;
import com.allcitizens.domain.request.Channel;
import com.allcitizens.domain.request.Priority;
import com.allcitizens.domain.request.RequestStatus;
import com.allcitizens.infrastructure.adapter.inbound.rest.request.dto.ServiceRequestResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.request.mapper.ServiceRequestRestMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServiceRequestController.class)
@AutoConfigureMockMvc(addFilters = false)
class ServiceRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateServiceRequestUseCase createUseCase;

    @MockBean
    private GetServiceRequestUseCase getUseCase;

    @MockBean
    private UpdateServiceRequestUseCase updateUseCase;

    @MockBean
    private ListServiceRequestsUseCase listUseCase;

    @MockBean
    private CloseServiceRequestUseCase closeUseCase;

    @MockBean
    private CancelServiceRequestUseCase cancelUseCase;

    @MockBean
    private ServiceRequestRestMapper mapper;

    private final UUID requestId = UUID.randomUUID();
    private final UUID tenantId = UUID.randomUUID();

    @Test
    void shouldCreateServiceRequest() throws Exception {
        var result = buildResult(RequestStatus.OPEN);
        var response = buildResponse("OPEN");

        when(mapper.toCommand(any(com.allcitizens.infrastructure.adapter.inbound.rest.request.dto.CreateServiceRequestRequest.class)))
                .thenReturn(buildCreateCommand());
        when(createUseCase.execute(any(CreateServiceRequestCommand.class))).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        var body = Map.of(
                "tenantId", tenantId,
                "protocol", "PROTO-001",
                "serviceId", UUID.randomUUID(),
                "channel", "PHONE",
                "priority", "NORMAL",
                "description", "Test"
        );

        mockMvc.perform(post("/api/v1/service-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.protocol").value("PROTO-001"));
    }

    @Test
    void shouldGetServiceRequest() throws Exception {
        var result = buildResult(RequestStatus.OPEN);
        var response = buildResponse("OPEN");

        when(getUseCase.execute(requestId)).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/api/v1/service-requests/{id}", requestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestId.toString()));
    }

    @Test
    void shouldListServiceRequests() throws Exception {
        var result = buildResult(RequestStatus.OPEN);
        var response = buildResponse("OPEN");

        when(listUseCase.execute(tenantId)).thenReturn(List.of(result));
        when(mapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/api/v1/service-requests")
                        .param("tenantId", tenantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].protocol").value("PROTO-001"));
    }

    @Test
    void shouldUpdateServiceRequest() throws Exception {
        var result = buildResult(RequestStatus.OPEN);
        var response = buildResponse("OPEN");

        when(mapper.toCommand(any(com.allcitizens.infrastructure.adapter.inbound.rest.request.dto.UpdateServiceRequestRequest.class)))
                .thenReturn(new UpdateServiceRequestCommand("Updated", null, null, null, null));
        when(updateUseCase.execute(any(UUID.class), any(UpdateServiceRequestCommand.class))).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        var body = Map.of("description", "Updated");

        mockMvc.perform(put("/api/v1/service-requests/{id}", requestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCloseServiceRequest() throws Exception {
        var result = buildResult(RequestStatus.CLOSED);
        var response = buildResponse("CLOSED");

        when(closeUseCase.execute(requestId)).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(post("/api/v1/service-requests/{id}/close", requestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CLOSED"));
    }

    @Test
    void shouldCancelServiceRequest() throws Exception {
        var result = buildResult(RequestStatus.CANCELLED);
        var response = buildResponse("CANCELLED");

        when(cancelUseCase.execute(requestId)).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(post("/api/v1/service-requests/{id}/cancel", requestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    private ServiceRequestResult buildResult(RequestStatus status) {
        return new ServiceRequestResult(
                requestId, tenantId, "PROTO-001", UUID.randomUUID(),
                null, null, null, null, null,
                Channel.PHONE, status, Priority.NORMAL,
                "Test", null, Map.of(), null, null,
                false, false, null, null, null, null, null,
                Instant.now(), Instant.now(), null
        );
    }

    private ServiceRequestResponse buildResponse(String status) {
        return new ServiceRequestResponse(
                requestId, tenantId, "PROTO-001", UUID.randomUUID(),
                null, null, null, null, null,
                "PHONE", status, "NORMAL",
                "Test", null, Map.of(), null, null,
                false, false, null, null, null, null, null,
                Instant.now(), Instant.now(), null
        );
    }

    private CreateServiceRequestCommand buildCreateCommand() {
        return new CreateServiceRequestCommand(
                tenantId, "PROTO-001", UUID.randomUUID(),
                null, null, Channel.PHONE, Priority.NORMAL,
                "Test", false, false, Map.of(), null, null
        );
    }
}
