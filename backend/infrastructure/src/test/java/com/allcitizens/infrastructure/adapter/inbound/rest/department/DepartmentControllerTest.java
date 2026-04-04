package com.allcitizens.infrastructure.adapter.inbound.rest.department;

import com.allcitizens.application.department.command.CreateDepartmentCommand;
import com.allcitizens.application.department.query.ListDepartmentsQuery;
import com.allcitizens.application.department.command.UpdateDepartmentCommand;
import com.allcitizens.application.department.result.DepartmentResult;
import com.allcitizens.domain.common.PageResult;
import com.allcitizens.application.department.usecase.CreateDepartmentUseCase;
import com.allcitizens.application.department.usecase.DeleteDepartmentUseCase;
import com.allcitizens.application.department.usecase.GetDepartmentUseCase;
import com.allcitizens.application.department.usecase.ListDepartmentsUseCase;
import com.allcitizens.application.department.usecase.UpdateDepartmentUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.department.dto.DepartmentResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.department.mapper.DepartmentRestMapper;
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

@WebMvcTest(DepartmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateDepartmentUseCase createDepartmentUseCase;

    @MockBean
    private GetDepartmentUseCase getDepartmentUseCase;

    @MockBean
    private UpdateDepartmentUseCase updateDepartmentUseCase;

    @MockBean
    private ListDepartmentsUseCase listDepartmentsUseCase;

    @MockBean
    private DeleteDepartmentUseCase deleteDepartmentUseCase;

    @MockBean
    private DepartmentRestMapper mapper;

    private final UUID departmentId = UUID.randomUUID();
    private final UUID tenantId = UUID.randomUUID();
    private final Instant now = Instant.now();

    private DepartmentResult sampleResult() {
        return new DepartmentResult(
            departmentId, tenantId, null, "Engineering", "ENG",
            "eng@test.com", true, true, false, null, 1, now, now
        );
    }

    private DepartmentResponse sampleResponse() {
        return new DepartmentResponse(
            departmentId, tenantId, null, "Engineering", "ENG",
            "eng@test.com", true, true, false, null, 1, now, now
        );
    }

    @Test
    void shouldCreateDepartment() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(mapper.toCommand(any(com.allcitizens.infrastructure.adapter.inbound.rest.department.dto.CreateDepartmentRequest.class)))
            .thenReturn(new CreateDepartmentCommand(
                tenantId, null, "Engineering", "ENG", "eng@test.com", true, false, null, 1
        ));
        when(createDepartmentUseCase.execute(any(CreateDepartmentCommand.class))).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        var requestBody = """
            {
                "tenantId": "%s",
                "name": "Engineering",
                "abbreviation": "ENG",
                "email": "eng@test.com",
                "canRespond": true,
                "isRoot": false,
                "displayOrder": 1
            }
            """.formatted(tenantId);

        mockMvc.perform(post("/api/v1/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Engineering"))
            .andExpect(jsonPath("$.abbreviation").value("ENG"));
    }

    @Test
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        var requestBody = """
            {
                "tenantId": "%s",
                "name": "",
                "displayOrder": 0
            }
            """.formatted(tenantId);

        mockMvc.perform(post("/api/v1/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetDepartmentById() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(getDepartmentUseCase.execute(departmentId)).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/api/v1/departments/{id}", departmentId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(departmentId.toString()))
            .andExpect(jsonPath("$.name").value("Engineering"));
    }

    @Test
    void shouldListDepartmentsByTenantId() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        var page = new PageResult<>(List.of(result), 1, 0, 20);
        when(listDepartmentsUseCase.execute(any(ListDepartmentsQuery.class)))
                .thenReturn(page);
        when(mapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/api/v1/departments").param("tenantId", tenantId.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].name").value("Engineering"));
    }

    @Test
    void shouldUpdateDepartment() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(mapper.toCommand(any(com.allcitizens.infrastructure.adapter.inbound.rest.department.dto.UpdateDepartmentRequest.class)))
            .thenReturn(new UpdateDepartmentCommand("Engineering", "ENG", "eng@test.com", true, null, null, 1));
        when(updateDepartmentUseCase.execute(eq(departmentId), any(UpdateDepartmentCommand.class))).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        var requestBody = """
            {
                "name": "Engineering",
                "abbreviation": "ENG",
                "email": "eng@test.com",
                "canRespond": true,
                "displayOrder": 1
            }
            """;

        mockMvc.perform(put("/api/v1/departments/{id}", departmentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Engineering"));
    }

    @Test
    void shouldDeleteDepartment() throws Exception {
        mockMvc.perform(delete("/api/v1/departments/{id}", departmentId))
            .andExpect(status().isNoContent());

        verify(deleteDepartmentUseCase).execute(departmentId);
    }
}
