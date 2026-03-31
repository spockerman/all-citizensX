package com.allcitizens.infrastructure.adapter.inbound.rest.address;

import com.allcitizens.application.address.usecase.CreateAddressUseCase;
import com.allcitizens.application.address.usecase.DeleteAddressUseCase;
import com.allcitizens.application.address.usecase.GetAddressUseCase;
import com.allcitizens.application.address.usecase.UpdateAddressUseCase;
import com.allcitizens.infrastructure.adapter.inbound.rest.address.dto.AddressResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.address.dto.CreateAddressRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.address.dto.UpdateAddressRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.address.mapper.AddressRestMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final CreateAddressUseCase createAddressUseCase;
    private final GetAddressUseCase getAddressUseCase;
    private final UpdateAddressUseCase updateAddressUseCase;
    private final DeleteAddressUseCase deleteAddressUseCase;
    private final AddressRestMapper mapper;

    public AddressController(CreateAddressUseCase createAddressUseCase,
                             GetAddressUseCase getAddressUseCase,
                             UpdateAddressUseCase updateAddressUseCase,
                             DeleteAddressUseCase deleteAddressUseCase,
                             AddressRestMapper mapper) {
        this.createAddressUseCase = createAddressUseCase;
        this.getAddressUseCase = getAddressUseCase;
        this.updateAddressUseCase = updateAddressUseCase;
        this.deleteAddressUseCase = deleteAddressUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<AddressResponse> create(@Valid @RequestBody CreateAddressRequest request) {
        var command = mapper.toCommand(request);
        var result = createAddressUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> getById(@PathVariable UUID id) {
        var result = getAddressUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> update(@PathVariable UUID id,
                                                  @RequestBody UpdateAddressRequest request) {
        var command = mapper.toCommand(request);
        var result = updateAddressUseCase.execute(id, command);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteAddressUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
