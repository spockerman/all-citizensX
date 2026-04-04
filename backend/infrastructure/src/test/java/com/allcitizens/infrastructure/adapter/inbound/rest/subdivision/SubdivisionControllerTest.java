package com.allcitizens.infrastructure.adapter.inbound.rest.subdivision;

import com.allcitizens.application.subdivision.command.CreateSubdivisionCommand;
import com.allcitizens.application.subdivision.query.ListSubdivisionsQuery;
import com.allcitizens.application.subdivision.command.UpdateSubdivisionCommand;
import com.allcitizens.application.subdivision.result.SubdivisionResult;
import com.allcitizens.domain.common.PageResult;
import com.allcitizens.application.subdivision.usecase.CreateSubdivisionUseCase;
import com.allcitizens.application.subdivision.usecase.DeleteSubdivisionUseCase;
import com.allcitizens.application.subdivision.usecase.GetSubdivisionUseCase;
import com.allcitizens.application.subdivision.usecase.ListSubdivisionsUseCase;
import com.allcitizens.application.subdivision.usecase.UpdateSubdivisionUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.subdivision.dto.SubdivisionResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.subdivision.mapper.SubdivisionRestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

@WebMvcTest(SubdivisionController.class)
@AutoConfigureMockMvc(addFilters = false)
class SubdivisionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateSubdivisionUseCase createSubdivisionUseCase;

    @MockBean
    private GetSubdivisionUseCase getSubdivisionUseCase;

    @MockBean
    private UpdateSubdivisionUseCase updateSubdivisionUseCase;

    @MockBean
    private ListSubdivisionsUseCase listSubdivisionsUseCase;

    @MockBean
    private DeleteSubdivisionUseCase deleteSubdivisionUseCase;

    @MockBean
    private SubdivisionRestMapper mapper;

    private final UUID subdivisionId = UUID.randomUUID();

    private SubdivisionResult sampleResult() {
        return new SubdivisionResult(subdivisionId, "North", true);
    }

    private SubdivisionResponse sampleResponse() {
        return new SubdivisionResponse(subdivisionId, "North", true);
    }

    @Test
    void shouldCreateSubdivision() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(mapper.toCommand(any(com.allcitizens.infrastructure.adapter.inbound.rest.subdivision.dto.CreateSubdivisionRequest.class)))
            .thenReturn(new CreateSubdivisionCommand("North"));
        when(createSubdivisionUseCase.execute(any(CreateSubdivisionCommand.class))).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        var requestBody = """
            {
                "name": "North"
            }
            """;

        mockMvc.perform(post("/api/v1/subdivisions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("North"));
    }

    @Test
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        var requestBody = """
            {
                "name": ""
            }
            """;

        mockMvc.perform(post("/api/v1/subdivisions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetSubdivisionById() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(getSubdivisionUseCase.execute(subdivisionId)).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/api/v1/subdivisions/{id}", subdivisionId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(subdivisionId.toString()))
            .andExpect(jsonPath("$.name").value("North"));
    }

    @Test
    void shouldListAllSubdivisions() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        var page = new PageResult<>(List.of(result), 1, 0, 20);
        when(listSubdivisionsUseCase.execute(any(ListSubdivisionsQuery.class))).thenReturn(page);
        when(mapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/api/v1/subdivisions"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].name").value("North"));
    }

    @Test
    void shouldUpdateSubdivision() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(mapper.toCommand(any(com.allcitizens.infrastructure.adapter.inbound.rest.subdivision.dto.UpdateSubdivisionRequest.class)))
            .thenReturn(new UpdateSubdivisionCommand("North", true));
        when(updateSubdivisionUseCase.execute(eq(subdivisionId), any(UpdateSubdivisionCommand.class))).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        var requestBody = """
            {
                "name": "North",
                "active": true
            }
            """;

        mockMvc.perform(put("/api/v1/subdivisions/{id}", subdivisionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("North"));
    }

    @Test
    void shouldDeleteSubdivision() throws Exception {
        mockMvc.perform(delete("/api/v1/subdivisions/{id}", subdivisionId))
            .andExpect(status().isNoContent());

        verify(deleteSubdivisionUseCase).execute(subdivisionId);
    }
}
