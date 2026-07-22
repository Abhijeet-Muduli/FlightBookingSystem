package com.ab.flightbooking.repository;

import com.ab.flightbooking.model.Airport;
import com.ab.flightbooking.util.JsonFileHandler;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JsonAirportRepository implements AirportRepository {

    private final Map<String, Airport> cache;

    public JsonAirportRepository(JsonFileHandler fileHandler, String classpathResource) {
        List<Airport> airports = fileHandler.readFromClasspath(classpathResource, new TypeReference<List<Airport>>() {});
        this.cache = airports.stream().collect(Collectors.toMap(Airport::id, Function.identity()));
    }

    @Override
    public List<Airport> findAll() {
        return List.copyOf(cache.values());
    }

    @Override
    public Optional<Airport> findById(String id) {
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public Optional<Airport> findByName(String name) {
        return cache.values().stream()
                .filter(a -> a.name().equalsIgnoreCase(name))
                .findFirst();
    }
}