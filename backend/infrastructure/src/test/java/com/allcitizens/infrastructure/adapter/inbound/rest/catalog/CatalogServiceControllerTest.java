package com.allcitizens.infrastructure.adapter.inbound.rest.catalog;

import com.allcitizens.application.catalog.command.CreateCatalogServiceCommand;
import com.allcitizens.application.catalog.query.ListCatalogServicesQuery;
import com.allcitizens.application.catalog.command.UpdateCatalogServiceCommand;
import com.allcitizens.application.catalog.result.CatalogServiceResult;
import com.allcitizens.domain.common.PageResult;
import com.allcitizens.application.catalog.usecase.CreateCatalogServiceUseCase;
import com.allcitizens.application.catalog.usecase.DeleteCatalogServiceUseCase;
import com.allcitizens.application.catalog.usecase.GetCatalogServiceUseCase;
import com.allcitizens.application.catalog.usecase.ListCatalogServicesUseCase;
import com.allcitizens.application.catalog.usecase.UpdateCatalogServiceUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.catalog.dto.CatalogServiceResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.catalog.mapper.CatalogServiceRestMapper;
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

@WebMvcTest(CatalogServiceController.class)
@AutoConfigureMockMvc(addFilters = false)
class CatalogServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateCatalogServiceUseCase createCatalogServiceUseCase;

    @MockBean
    private GetCatalogServiceUseCase getCatalogServiceUseCase;

    @MockBean
    private UpdateCatalogServiceUseCase updateCatalogServiceUseCase;

    @MockBean
    private ListCatalogServicesUseCase listCatalogServicesUseCase;

    @MockBean
    private DeleteCatalogServiceUseCase deleteCatalogServiceUseCase;

    @MockBean
    private CatalogServiceRestMapper mapper;

    private final UUID catalogServiceId = UUID.randomUUID();
    private final UUID tenantId = UUID.randomUUID();
    private final UUID subjectId = UUID.randomUUID();
    private final UUID subdivisionId = UUID.randomUUID();
    private final UUID departmentId = UUID.randomUUID();
    private final Instant now = Instant.now();

    private CatalogServiceResult sampleResult() {
        return new CatalogServiceResult(
            catalogServiceId,
            tenantId,
            subjectId,
            subdivisionId,
            departmentId,
            "Leak report",
            "Report water leaks",
            5,
            "NORMAL",
            false,
            true,
            true,
            true,
            Map.of("key", "value"),
            true,
            now,
            now
        );
    }

    private CatalogServiceResponse sampleResponse() {
        return new CatalogServiceResponse(
            catalogServiceId,
            tenantId,
            subjectId,
            subdivisionId,
            departmentId,
            "Leak report",
            "Report water leaks",
            5,
            "NORMAL",
            false,
            true,
            true,
            true,
            Map.of("key", "value"),
            true,
            now,
            now
        );
    }

    @Test
    void shouldCreateCatalogService() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(mapper.toCommand(any(com.allcitizens.infrastructure.adapter.inbound.rest.catalog.dto.CreateCatalogServiceRequest.class)))
            .thenReturn(new CreateCatalogServiceCommand(
                tenantId,
                subjectId,
                subdivisionId,
                departmentId,
                "Leak report",
                "Report water leaks",
                5,
                "NORMAL",
                false,
                true,
                true,
                true
            ));
        when(createCatalogServiceUseCase.execute(any(CreateCatalogServiceCommand.class))).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        var requestBody = """
            {
                "tenantId": "%s",
                "subjectId": "%s",
                "subdivisionId": "%s",
                "departmentId": "%s",
                "displayName": "Leak report",
                "description": "Report water leaks",
                "slaDays": 5,
                "defaultPriority": "NORMAL",
                "allowsAnonymous": false,
                "allowsMultiForward": true,
                "visibleWeb": true,
                "visibleApp": true
            }
            """.formatted(tenantId, subjectId, subdivisionId, departmentId);

        mockMvc.perform(post("/api/v1/catalog-services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.displayName").value("Leak report"));
    }

    @Test
    void shouldReturnBadRequestWhenTenantIdIsNull() throws Exception {
        var requestBody = """
            {
                "subjectId": "%s",
                "subdivisionId": "%s",
                "slaDays": 5,
                "defaultPriority": "NORMAL",
                "allowsAnonymous": false,
                "allowsMultiForward": true,
                "visibleWeb": true,
                "visibleApp": true
            }
            """.formatted(subjectId, subdivisionId);

        mockMvc.perform(post("/api/v1/catalog-services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetCatalogServiceById() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(getCatalogServiceUseCase.execute(catalogServiceId)).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/api/v1/catalog-services/{id}", catalogServiceId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(catalogServiceId.toString()))
            .andExpect(jsonPath("$.displayName").value("Leak report"));
    }

    @Test
    void shouldListCatalogServicesByTenantId() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        var page = new PageResult<>(List.of(result), 1, 0, 20);
        when(listCatalogServicesUseCase.execute(any(ListCatalogServicesQuery.class))).thenReturn(page);
        when(mapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/api/v1/catalog-services").param("tenantId", tenantId.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].displayName").value("Leak report"));
    }

    @Test
    void shouldUpdateCatalogService() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(mapper.toCommand(any(com.allcitizens.infrastructure.adapter.inbound.rest.catalog.dto.UpdateCatalogServiceRequest.class)))
            .thenReturn(new UpdateCatalogServiceCommand(
                "Leak report",
                "Report water leaks",
                5,
                "NORMAL",
                false,
                true,
                true,
                true,
                true,
                departmentId
            ));
        when(updateCatalogServiceUseCase.execute(eq(catalogServiceId), any(UpdateCatalogServiceCommand.class)))
            .thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        var requestBody = """
            {
                "displayName": "Leak report",
                "description": "Report water leaks",
                "slaDays": 5,
                "defaultPriority": "NORMAL",
                "allowsAnonymous": false,
                "allowsMultiForward": true,
                "visibleWeb": true,
                "visibleApp": true,
                "active": true,
                "departmentId": "%s"
            }
            """.formatted(departmentId);

        mockMvc.perform(put("/api/v1/catalog-services/{id}", catalogServiceId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.displayName").value("Leak report"));
    }

    @Test
    void shouldDeleteCatalogService() throws Exception {
        mockMvc.perform(delete("/api/v1/catalog-services/{id}", catalogServiceId))
            .andExpect(status().isNoContent());

        verify(deleteCatalogServiceUseCase).execute(catalogServiceId);
    }
}
