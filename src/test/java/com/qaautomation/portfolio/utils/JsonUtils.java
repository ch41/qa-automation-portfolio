package com.qaautomation.portfolio.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public final class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {
    }

    public static JsonNode readTree(String resourcePath) {
        try (InputStream stream = openStream(resourcePath)) {
            return MAPPER.readTree(stream);
        } catch (IOException exception) {
            throw new UncheckedIOException("Unable to read resource: " + resourcePath, exception);
        }
    }

    public static <T> T readValue(String resourcePath, Class<T> type) {
        try (InputStream stream = openStream(resourcePath)) {
            return MAPPER.readValue(stream, type);
        } catch (IOException exception) {
            throw new UncheckedIOException("Unable to read resource: " + resourcePath, exception);
        }
    }

    private static InputStream openStream(String resourcePath) {
        InputStream stream = JsonUtils.class.getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        return stream;
    }
}
