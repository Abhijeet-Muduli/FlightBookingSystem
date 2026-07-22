package com.ab.flightbooking.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class ObjectMapperProvider {

    private static final ObjectMapper INSTANCE = build();

    private ObjectMapperProvider() {
    }

    public static ObjectMapper getMapper() {
        return INSTANCE;
    }

    private static ObjectMapper build() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}