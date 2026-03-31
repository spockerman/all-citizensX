package com.allcitizens.infrastructure.adapter.inbound.rest.tenant;

import com.allcitizens.application.tenant.command.CreateTenantCommand;
import com.allcitizens.application.tenant.command.UpdateTenantCommand;
import com.allcitizens.application.tenant.result.TenantResult;
import com.allcitizens.application.tenant.usecase.CreateTenantUseCase;
import com.allcitizens.application.tenant.usecase.DeleteTenantUseCase;
import com.allcitizens.application.tenant.usecase.GetTenantUseCase;
import com.allcitizens.application.tenant.usecase.ListTenantsUseCase;
import com.allcitizens.application.tenant.usecase.UpdateTenantUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.tenant.dto.TenantResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.tenant.mapper.TenantRestMapper;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TenantController.class)
@AutoConfigureMockMvc(addFilters = false)
class TenantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateTenantUseCase createTenantUseCase;

    @MockBean
    private GetTenantUseCase getTenantUseCase;

    @MockBean
    private UpdateTenantUseCase updateTenantUseCase;

    @MockBean
    private ListTenantsUseCase listTenantsUseCase;

    @MockBean
    private DeleteTenantUseCase deleteTenantUseCase;

    @MockBean
    private TenantRestMapper mapper;

    private final UUID tenantId = UUID.randomUUID();
    private final Instant now = Instant.now();

    private TenantResult sampleResult() {
        return new TenantResult(
            tenantId, "City of Springfield", "SPR", true, Map.of(), now, now
        );
    }

    private TenantResponse sampleResponse() {
        return new TenantResponse(
            tenantId, "City of Springfield", "SPR", true, Map.of(), now, now
        );
    }

    @Test
    void shouldCreateTenant() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(mapper.toCommand(any(com.allcitizens.infrastructure.adapter.inbound.rest.tenant.dto.CreateTenantRequest.class)))
            .thenReturn(new CreateTenantCommand("City of Springfield", "SPR"));
        when(createTenantUseCase.execute(any(CreateTenantCommand.class))).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        var requestBody = """
            {
                "name": "City of Springfield",
                "code": "SPR"
            }
            """;

        mockMvc.perform(post("/api/v1/tenants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("City of Springfield"))
            .andExpect(jsonPath("$.code").value("SPR"));
    }

    @Test
    void shouldListTenants() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(listTenantsUseCase.execute()).thenReturn(List.of(result));
        when(mapper.toResponseList(List.of(result))).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/tenants"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("City of Springfield"));
    }

    @Test
    void shouldGetTenantById() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(getTenantUseCase.execute(tenantId)).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/api/v1/tenants/{id}", tenantId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(tenantId.toString()))
            .andExpect(jsonPath("$.name").value("City of Springfield"));
    }

    @Test
    void shouldUpdateTenant() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(mapper.toCommand(any(com.allcitizens.infrastructure.adapter.inbound.rest.tenant.dto.UpdateTenantRequest.class)))
            .thenReturn(new UpdateTenantCommand("City of Springfield", null));
        when(updateTenantUseCase.execute(eq(tenantId), any(UpdateTenantCommand.class))).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        var requestBody = """
            {
                "name": "City of Springfield"
            }
            """;

        mockMvc.perform(put("/api/v1/tenants/{id}", tenantId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("City of Springfield"));
    }

    @Test
    void shouldDeleteTenant() throws Exception {
        mockMvc.perform(delete("/api/v1/tenants/{id}", tenantId))
            .andExpect(status().isNoContent());

        verify(deleteTenantUseCase).execute(tenantId);
    }
}
