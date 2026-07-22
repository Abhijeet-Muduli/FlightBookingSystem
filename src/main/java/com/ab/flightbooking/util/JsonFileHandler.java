package com.ab.flightbooking.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class JsonFileHandler {

    private static final Logger log = LoggerFactory.getLogger(JsonFileHandler.class);

    private final ObjectMapper mapper;

    public JsonFileHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public <T> List<T> readFromClasspath(String resourceName, TypeReference<List<T>> typeRef) {
        log.debug("Loading seed data from classpath resource: {}", resourceName);
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (in == null) {
                throw new IOException("Resource not found on classpath: " + resourceName);
            }
            return mapper.readValue(in, typeRef);
        } catch (IOException e) {
            throw new UncheckedIOExceptionWrapper("Failed to read classpath resource: " + resourceName, e);
        }
    }

    public <T> List<T> readFromFile(Path filePath, TypeReference<List<T>> typeRef) {
        if (!Files.exists(filePath)) {
            log.info("File {} does not exist yet - starting with an empty list", filePath);
            return Collections.emptyList();
        }
        log.debug("Loading data from file: {}", filePath);
        try {
            return mapper.readValue(filePath.toFile(), typeRef);
        } catch (IOException e) {
            throw new UncheckedIOExceptionWrapper("Failed to read file: " + filePath, e);
        }
    }

    public <T> void writeToFile(Path filePath, List<T> data) {
        log.debug("Persisting {} record(s) to {}", data.size(), filePath);
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), data);
        } catch (IOException e) {
            throw new UncheckedIOExceptionWrapper("Failed to write file: " + filePath, e);
        }
    }

    public static class UncheckedIOExceptionWrapper extends RuntimeException {
        public UncheckedIOExceptionWrapper(String message, Throwable cause) {
            super(message, cause);
        }
    }
}