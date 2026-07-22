package com.ab.flightbooking.repository;

import com.ab.flightbooking.model.Airplane;
import com.ab.flightbooking.util.JsonFileHandler;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JsonAirplaneRepository implements AirplaneRepository {

    private final Map<String, Airplane> cache;

    public JsonAirplaneRepository(JsonFileHandler fileHandler, String classpathResource) {
        List<Airplane> airplanes = fileHandler.readFromClasspath(classpathResource, new TypeReference<List<Airplane>>() {});
        this.cache = airplanes.stream().collect(Collectors.toMap(Airplane::id, Function.identity()));
    }

    @Override
    public List<Airplane> findAll() {
        return List.copyOf(cache.values());
    }

    @Override
    public Optional<Airplane> findById(String id) {
        return Optional.ofNullable(cache.get(id));
    }
}