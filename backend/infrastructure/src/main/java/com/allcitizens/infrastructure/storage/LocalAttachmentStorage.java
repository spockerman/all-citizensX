package com.allcitizens.infrastructure.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class LocalAttachmentStorage {

    private final Path basePath;

    public LocalAttachmentStorage(
            @Value("${app.attachments.directory:${user.dir}/data/attachments}") String directory) {
        this.basePath = Paths.get(directory).toAbsolutePath().normalize();
    }

    /**
     * Stores the uploaded file and returns a relative storage key (safe file name under base path).
     */
    public String store(MultipartFile file) {
        try {
            Files.createDirectories(basePath);
            var original = file.getOriginalFilename();
            var suffix = (original != null && !original.isBlank()) ? sanitize(original) : "file";
            var name = UUID.randomUUID() + "_" + suffix;
            var target = basePath.resolve(name);
            file.transferTo(target);
            return name;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Path resolveStoredFile(String storageKey) {
        var normalized = Paths.get(storageKey).getFileName();
        return basePath.resolve(normalized).normalize();
    }

    public void deleteIfExists(String storageKey) {
        try {
            var path = resolveStoredFile(storageKey);
            if (path.startsWith(basePath) && Files.isRegularFile(path)) {
                Files.deleteIfExists(path);
            }
        } catch (IOException ignored) {
            // best effort
        }
    }

    private static String sanitize(String name) {
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
