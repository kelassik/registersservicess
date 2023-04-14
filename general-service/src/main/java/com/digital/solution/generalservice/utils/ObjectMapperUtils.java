package com.digital.solution.generalservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Slf4j
@SuppressWarnings("all")
public class ObjectMapperUtils {

    @Autowired
    private ObjectMapper objectMapper;

    public String writeValueAsString(Object object) {
        return writeValueAsString(object, false);
    }

    public String writeValueAsString(Object object, boolean isPrettyPrint) {
        try {
            if (isPrettyPrint) {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            }

            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error objectMapper.writeValueAsString: {}", e.toString(), e);
            return null;
        }
    }

    public byte[] writeValueAsBytes(Object object) {
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            log.error("Error objectMapper.writeValueAsString: {}", e.toString(), e);
            return new byte[0];
        }
    }

    public <T> T readValue(String content, Class<T> valueType) {
        return readValue(content, valueType, null);
    }

    public <T> T readValue(String content, Class<T> responseClass, T defaultValue) {
        if (content == null) {
            return defaultValue;
        }

        try {
            return objectMapper.readValue(content, responseClass);
        } catch (IOException e) {
            log.error("Exception objectMapper.readValue: {}", e.toString(), e);
            return defaultValue;
        }
    }

    public <T> T readValue(String content, TypeReference<T> valueTypeRef) {
        return readValue(content, valueTypeRef, null);
    }

    public <T> T readValue(String content, TypeReference<T> valueTypeRef, T defaultValue) {
        if (content == null) {
            return defaultValue;
        }

        try {
            return objectMapper.readValue(content, valueTypeRef);
        } catch (IOException e) {
            log.error("Exception objectMapper.readValue valueTypeRef: {}", e.toString(), e);
            return defaultValue;
        }
    }

    public <T> T readValue(byte[] content, Class<T> responseClass, T defaultValue) {
        if (content == null) {
            return defaultValue;
        }

        try {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(content, responseClass);
        } catch (IOException e) {
            log.error("Exception objectMapper.readValue: {}", e.toString(), e);
            return defaultValue;
        }
    }

    public <T> T readValue(byte[] content, Class<T> valueType) {
        return readValue(content, valueType, null);
    }

    public JsonNode readTree(String content, JsonNode defaultValue) {
        try {
            return objectMapper.readTree(content);
        } catch (IOException e) {
            log.error("Exception objectMapper.readTree valueTypeRef: {}", e.toString(), e);
            return defaultValue;
        }
    }

    public JsonNode readTree(String content) {
        return readTree(content, null);
    }
}
