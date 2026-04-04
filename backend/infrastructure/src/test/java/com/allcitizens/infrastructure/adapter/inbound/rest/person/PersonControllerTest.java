package com.allcitizens.infrastructure.adapter.inbound.rest.person;

import com.allcitizens.application.person.command.CreatePersonCommand;
import com.allcitizens.application.person.query.ListPersonsQuery;
import com.allcitizens.application.person.command.UpdatePersonCommand;
import com.allcitizens.application.person.result.PersonResult;
import com.allcitizens.domain.common.PageResult;
import com.allcitizens.application.person.usecase.CreatePersonUseCase;
import com.allcitizens.application.person.usecase.DeletePersonUseCase;
import com.allcitizens.application.person.usecase.GetPersonUseCase;
import com.allcitizens.application.person.usecase.ListPersonsUseCase;
import com.allcitizens.application.person.usecase.UpdatePersonUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.person.dto.CreatePersonRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.person.dto.PersonResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.person.dto.UpdatePersonRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.person.mapper.PersonRestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
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

@WebMvcTest(PersonController.class)
@AutoConfigureMockMvc(addFilters = false)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreatePersonUseCase createPersonUseCase;

    @MockBean
    private GetPersonUseCase getPersonUseCase;

    @MockBean
    private UpdatePersonUseCase updatePersonUseCase;

    @MockBean
    private ListPersonsUseCase listPersonsUseCase;

    @MockBean
    private DeletePersonUseCase deletePersonUseCase;

    @MockBean
    private PersonRestMapper mapper;

    private final UUID personId = UUID.randomUUID();
    private final UUID tenantId = UUID.randomUUID();
    private final Instant now = Instant.now();

    private PersonResult sampleResult() {
        return new PersonResult(
            personId,
            tenantId,
            "INDIVIDUAL",
            "Jane Doe",
            "jane@example.com",
            "12345678901234",
            null,
            LocalDate.of(1990, 1, 1),
            "NOT_INFORMED",
            null,
            null,
            true,
            now,
            now
        );
    }

    private PersonResponse sampleResponse() {
        return new PersonResponse(
            personId,
            tenantId,
            "INDIVIDUAL",
            "Jane Doe",
            "jane@example.com",
            "12345678901234",
            null,
            LocalDate.of(1990, 1, 1),
            "NOT_INFORMED",
            null,
            null,
            true,
            now,
            now
        );
    }

    @Test
    void shouldCreatePerson() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(mapper.toCommand(any(CreatePersonRequest.class)))
            .thenReturn(new CreatePersonCommand(
                tenantId, "Jane Doe", "jane@example.com", "12345678901234",
                null, null, null, null
            ));
        when(createPersonUseCase.execute(any(CreatePersonCommand.class))).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        var requestBody = """
            {
                "tenantId": "%s",
                "fullName": "Jane Doe",
                "email": "jane@example.com",
                "taxId": "12345678901234"
            }
            """.formatted(tenantId);

        mockMvc.perform(post("/api/v1/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.fullName").value("Jane Doe"));
    }

    @Test
    void shouldReturnBadRequestWhenFullNameIsBlank() throws Exception {
        var requestBody = """
            {
                "tenantId": "%s",
                "fullName": "",
                "email": "x@y.com"
            }
            """.formatted(tenantId);

        mockMvc.perform(post("/api/v1/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenTenantIdMissing() throws Exception {
        var requestBody = """
            {
                "fullName": "A",
                "email": "x@y.com"
            }
            """;

        mockMvc.perform(post("/api/v1/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetPersonById() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(getPersonUseCase.execute(personId)).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/api/v1/persons/{id}", personId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(personId.toString()))
            .andExpect(jsonPath("$.fullName").value("Jane Doe"));
    }

    @Test
    void shouldListPersonsByTenantId() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        var page = new PageResult<>(List.of(result), 1, 0, 20);
        when(listPersonsUseCase.execute(any(ListPersonsQuery.class))).thenReturn(page);
        when(mapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/api/v1/persons").param("tenantId", tenantId.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].fullName").value("Jane Doe"));
    }

    @Test
    void shouldUpdatePerson() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(mapper.toCommand(any(UpdatePersonRequest.class)))
            .thenReturn(new UpdatePersonCommand(
                "Jane Doe", "jane@example.com", "12345678901234", null, null, null, null, true
            ));
        when(updatePersonUseCase.execute(eq(personId), any(UpdatePersonCommand.class))).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        var requestBody = """
            {
                "fullName": "Jane Doe",
                "email": "jane@example.com",
                "taxId": "12345678901234",
                "active": true
            }
            """;

        mockMvc.perform(put("/api/v1/persons/{id}", personId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fullName").value("Jane Doe"));
    }

    @Test
    void shouldDeletePerson() throws Exception {
        mockMvc.perform(delete("/api/v1/persons/{id}", personId))
            .andExpect(status().isNoContent());

        verify(deletePersonUseCase).execute(personId);
    }
}
