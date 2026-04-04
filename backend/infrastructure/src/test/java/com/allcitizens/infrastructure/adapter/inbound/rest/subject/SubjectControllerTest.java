package com.allcitizens.infrastructure.adapter.inbound.rest.subject;

import com.allcitizens.application.subject.command.CreateSubjectCommand;
import com.allcitizens.application.subject.query.ListSubjectsQuery;
import com.allcitizens.application.subject.command.UpdateSubjectCommand;
import com.allcitizens.application.subject.result.SubjectResult;
import com.allcitizens.domain.common.PageResult;
import com.allcitizens.application.subject.usecase.CreateSubjectUseCase;
import com.allcitizens.application.subject.usecase.DeleteSubjectUseCase;
import com.allcitizens.application.subject.usecase.GetSubjectUseCase;
import com.allcitizens.application.subject.usecase.ListSubjectsUseCase;
import com.allcitizens.application.subject.usecase.UpdateSubjectUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.subject.dto.SubjectResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.subject.mapper.SubjectRestMapper;
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

@WebMvcTest(SubjectController.class)
@AutoConfigureMockMvc(addFilters = false)
class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateSubjectUseCase createSubjectUseCase;

    @MockBean
    private GetSubjectUseCase getSubjectUseCase;

    @MockBean
    private UpdateSubjectUseCase updateSubjectUseCase;

    @MockBean
    private ListSubjectsUseCase listSubjectsUseCase;

    @MockBean
    private DeleteSubjectUseCase deleteSubjectUseCase;

    @MockBean
    private SubjectRestMapper mapper;

    private final UUID subjectId = UUID.randomUUID();
    private final UUID tenantId = UUID.randomUUID();
    private final UUID departmentId = UUID.randomUUID();
    private final Instant now = Instant.now();

    private SubjectResult sampleResult() {
        return new SubjectResult(
            subjectId, tenantId, departmentId, "Water", true, true, true, now, now
        );
    }

    private SubjectResponse sampleResponse() {
        return new SubjectResponse(
            subjectId, tenantId, departmentId, "Water", true, true, true, now, now
        );
    }

    @Test
    void shouldCreateSubject() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(mapper.toCommand(any(com.allcitizens.infrastructure.adapter.inbound.rest.subject.dto.CreateSubjectRequest.class)))
            .thenReturn(new CreateSubjectCommand(
                tenantId, departmentId, "Water", true, true
            ));
        when(createSubjectUseCase.execute(any(CreateSubjectCommand.class))).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        var requestBody = """
            {
                "tenantId": "%s",
                "departmentId": "%s",
                "name": "Water",
                "visibleWeb": true,
                "visibleApp": true
            }
            """.formatted(tenantId, departmentId);

        mockMvc.perform(post("/api/v1/subjects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Water"));
    }

    @Test
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        var requestBody = """
            {
                "tenantId": "%s",
                "name": "",
                "visibleWeb": true,
                "visibleApp": true
            }
            """.formatted(tenantId);

        mockMvc.perform(post("/api/v1/subjects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetSubjectById() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(getSubjectUseCase.execute(subjectId)).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/api/v1/subjects/{id}", subjectId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(subjectId.toString()))
            .andExpect(jsonPath("$.name").value("Water"));
    }

    @Test
    void shouldListSubjectsByTenantId() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        var page = new PageResult<>(List.of(result), 1, 0, 20);
        when(listSubjectsUseCase.execute(any(ListSubjectsQuery.class))).thenReturn(page);
        when(mapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/api/v1/subjects").param("tenantId", tenantId.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].name").value("Water"));
    }

    @Test
    void shouldUpdateSubject() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(mapper.toCommand(any(com.allcitizens.infrastructure.adapter.inbound.rest.subject.dto.UpdateSubjectRequest.class)))
            .thenReturn(new UpdateSubjectCommand("Water", departmentId, true, true, true));
        when(updateSubjectUseCase.execute(eq(subjectId), any(UpdateSubjectCommand.class))).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        var requestBody = """
            {
                "name": "Water",
                "departmentId": "%s",
                "active": true,
                "visibleWeb": true,
                "visibleApp": true
            }
            """.formatted(departmentId);

        mockMvc.perform(put("/api/v1/subjects/{id}", subjectId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Water"));
    }

    @Test
    void shouldDeleteSubject() throws Exception {
        mockMvc.perform(delete("/api/v1/subjects/{id}", subjectId))
            .andExpect(status().isNoContent());

        verify(deleteSubjectUseCase).execute(subjectId);
    }
}
