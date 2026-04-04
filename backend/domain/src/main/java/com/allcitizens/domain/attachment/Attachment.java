package com.allcitizens.domain.attachment;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Attachment {

    private UUID id;
    private UUID requestId;
    private AttachmentType type;
    private String fileName;
    private String contentType;
    private Long sizeBytes;
    private String storagePath;
    private String thumbnailPath;
    private UUID userId;
    private Instant createdAt;

    private Attachment() {
    }

    public static Attachment create(UUID requestId, AttachmentType type, String fileName,
                                    String contentType, Long sizeBytes, String storagePath,
                                    String thumbnailPath, UUID userId) {
        Objects.requireNonNull(requestId, "requestId must not be null");
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(fileName, "fileName must not be null");
        Objects.requireNonNull(storagePath, "storagePath must not be null");

        var a = new Attachment();
        a.id = UUID.randomUUID();
        a.requestId = requestId;
        a.type = type;
        a.fileName = fileName;
        a.contentType = contentType;
        a.sizeBytes = sizeBytes;
        a.storagePath = storagePath;
        a.thumbnailPath = thumbnailPath;
        a.userId = userId;
        a.createdAt = Instant.now();
        return a;
    }

    public static Attachment reconstitute(UUID id, UUID requestId, AttachmentType type,
                                          String fileName, String contentType, Long sizeBytes,
                                          String storagePath, String thumbnailPath,
                                          UUID userId, Instant createdAt) {
        var a = new Attachment();
        a.id = id;
        a.requestId = requestId;
        a.type = type;
        a.fileName = fileName;
        a.contentType = contentType;
        a.sizeBytes = sizeBytes;
        a.storagePath = storagePath;
        a.thumbnailPath = thumbnailPath;
        a.userId = userId;
        a.createdAt = createdAt;
        return a;
    }

    public UUID getId() {
        return id;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public AttachmentType getType() {
        return type;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public Long getSizeBytes() {
        return sizeBytes;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public UUID getUserId() {
        return userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
