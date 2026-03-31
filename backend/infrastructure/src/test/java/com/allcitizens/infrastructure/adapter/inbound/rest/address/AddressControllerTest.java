package com.allcitizens.infrastructure.adapter.inbound.rest.address;

import com.allcitizens.application.address.command.CreateAddressCommand;
import com.allcitizens.application.address.command.UpdateAddressCommand;
import com.allcitizens.application.address.result.AddressResult;
import com.allcitizens.application.address.usecase.CreateAddressUseCase;
import com.allcitizens.application.address.usecase.DeleteAddressUseCase;
import com.allcitizens.application.address.usecase.GetAddressUseCase;
import com.allcitizens.application.address.usecase.UpdateAddressUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.address.dto.AddressResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.address.mapper.AddressRestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
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

@WebMvcTest(AddressController.class)
@AutoConfigureMockMvc(addFilters = false)
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateAddressUseCase createAddressUseCase;

    @MockBean
    private GetAddressUseCase getAddressUseCase;

    @MockBean
    private UpdateAddressUseCase updateAddressUseCase;

    @MockBean
    private DeleteAddressUseCase deleteAddressUseCase;

    @MockBean
    private AddressRestMapper mapper;

    private final UUID addressId = UUID.randomUUID();
    private final UUID cityId = UUID.randomUUID();
    private final Instant now = Instant.now();

    private AddressResult sampleResult() {
        return new AddressResult(
            addressId, "01310100", "Av. Paulista", "1000", null, null, cityId, "SP",
            null, -23.56, -46.65, now, now
        );
    }

    private AddressResponse sampleResponse() {
        return new AddressResponse(
            addressId, "01310100", "Av. Paulista", "1000", null, null, cityId, "SP",
            null, -23.56, -46.65, now, now
        );
    }

    @Test
    void shouldCreateAddress() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(mapper.toCommand(any(com.allcitizens.infrastructure.adapter.inbound.rest.address.dto.CreateAddressRequest.class)))
            .thenReturn(new CreateAddressCommand(
                cityId, "SP", "01310100", "Av. Paulista", "1000", null, null, null, null, null
            ));
        when(createAddressUseCase.execute(any(CreateAddressCommand.class))).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        var requestBody = """
            {
                "cityId": "%s",
                "stateCode": "SP",
                "zipCode": "01310100",
                "street": "Av. Paulista",
                "number": "1000"
            }
            """.formatted(cityId);

        mockMvc.perform(post("/api/v1/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.street").value("Av. Paulista"))
            .andExpect(jsonPath("$.stateCode").value("SP"));
    }

    @Test
    void shouldReturnBadRequestWhenStateCodeIsBlank() throws Exception {
        var requestBody = """
            {
                "cityId": "%s",
                "stateCode": ""
            }
            """.formatted(cityId);

        mockMvc.perform(post("/api/v1/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenCityIdIsNull() throws Exception {
        var requestBody = """
            {
                "stateCode": "SP"
            }
            """;

        mockMvc.perform(post("/api/v1/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetAddressById() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(getAddressUseCase.execute(addressId)).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/api/v1/addresses/{id}", addressId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(addressId.toString()))
            .andExpect(jsonPath("$.street").value("Av. Paulista"));
    }

    @Test
    void shouldUpdateAddress() throws Exception {
        var result = sampleResult();
        var response = sampleResponse();

        when(mapper.toCommand(any(com.allcitizens.infrastructure.adapter.inbound.rest.address.dto.UpdateAddressRequest.class)))
            .thenReturn(new UpdateAddressCommand(
                "02000", "New St", "2", null, null, null, null, null
            ));
        when(updateAddressUseCase.execute(eq(addressId), any(UpdateAddressCommand.class))).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        var requestBody = """
            {
                "zipCode": "02000",
                "street": "New St",
                "number": "2"
            }
            """;

        mockMvc.perform(put("/api/v1/addresses/{id}", addressId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.street").value("Av. Paulista"));
    }

    @Test
    void shouldDeleteAddress() throws Exception {
        mockMvc.perform(delete("/api/v1/addresses/{id}", addressId))
            .andExpect(status().isNoContent());

        verify(deleteAddressUseCase).execute(addressId);
    }
}
