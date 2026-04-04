package com.allcitizens.infrastructure.adapter.inbound.rest.attachment;

import com.allcitizens.application.attachment.usecase.CreateAttachmentUseCase;
import com.allcitizens.application.attachment.usecase.DeleteAttachmentUseCase;
import com.allcitizens.application.attachment.usecase.GetAttachmentUseCase;
import com.allcitizens.application.attachment.usecase.ListAttachmentsByRequestUseCase;
import com.allcitizens.domain.attachment.AttachmentType;
import com.allcitizens.infrastructure.adapter.inbound.rest.attachment.dto.AttachmentResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.attachment.mapper.AttachmentRestMapper;
import com.allcitizens.infrastructure.storage.LocalAttachmentStorage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
public class AttachmentController {

    private final CreateAttachmentUseCase createUseCase;
    private final GetAttachmentUseCase getUseCase;
    private final ListAttachmentsByRequestUseCase listByRequestUseCase;
    private final DeleteAttachmentUseCase deleteUseCase;
    private final AttachmentRestMapper mapper;
    private final LocalAttachmentStorage storage;

    public AttachmentController(CreateAttachmentUseCase createUseCase,
                                GetAttachmentUseCase getUseCase,
                                ListAttachmentsByRequestUseCase listByRequestUseCase,
                                DeleteAttachmentUseCase deleteUseCase,
                                AttachmentRestMapper mapper,
                                LocalAttachmentStorage storage) {
        this.createUseCase = createUseCase;
        this.getUseCase = getUseCase;
        this.listByRequestUseCase = listByRequestUseCase;
        this.deleteUseCase = deleteUseCase;
        this.mapper = mapper;
        this.storage = storage;
    }

    @PostMapping(path = "/api/v1/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentResponse> upload(
            @RequestPart("file") MultipartFile file,
            @RequestParam UUID requestId,
            @RequestParam AttachmentType type,
            @RequestParam(required = false) UUID userId) {
        var storageKey = storage.store(file);
        var originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
        var cmd = mapper.toCommand(
                requestId,
                type,
                originalName,
                file.getContentType(),
                file.getSize(),
                storageKey,
                null,
                userId
        );
        var result = createUseCase.execute(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping("/api/v1/attachments/{id}")
    public ResponseEntity<AttachmentResponse> get(@PathVariable UUID id) {
        var result = getUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @GetMapping("/api/v1/service-requests/{requestId}/attachments")
    public ResponseEntity<List<AttachmentResponse>> listByRequest(@PathVariable UUID requestId) {
        var results = listByRequestUseCase.execute(requestId);
        var body = results.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/api/v1/attachments/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        var existing = getUseCase.execute(id);
        deleteUseCase.execute(id);
        storage.deleteIfExists(existing.storagePath());
        return ResponseEntity.noContent().build();
    }
}
