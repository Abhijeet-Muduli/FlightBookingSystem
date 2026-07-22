package com.ab.flightbooking.repository;

import com.ab.flightbooking.model.Flight;
import com.ab.flightbooking.util.JsonFileHandler;
import com.fasterxml.jackson.core.type.TypeReference;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JsonFlightRepository implements FlightRepository {

    private final JsonFileHandler fileHandler;
    private final Path filePath;
    private final Map<String, Flight> cache;

    public JsonFlightRepository(JsonFileHandler fileHandler, Path filePath) {
        this.fileHandler = fileHandler;
        this.filePath = filePath;
        List<Flight> flights = fileHandler.readFromFile(filePath, new TypeReference<List<Flight>>() {});
        this.cache = flights.stream().collect(Collectors.toMap(Flight::id, Function.identity()));
    }

    @Override
    public List<Flight> findAll() {
        return List.copyOf(cache.values());
    }

    @Override
    public Optional<Flight> findById(String id) {
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public List<Flight> findByOriginAirportId(String airportId) {
        return cache.values().stream()
                .filter(f -> f.originAirportId().equals(airportId))
                .collect(Collectors.toList());
    }

    public Flight save(Flight flight) {
        cache.put(flight.id(), flight);
        fileHandler.writeToFile(filePath, List.copyOf(cache.values()));
        return flight;
    }
}