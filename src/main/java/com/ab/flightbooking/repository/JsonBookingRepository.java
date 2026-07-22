package com.ab.flightbooking.repository;

import com.ab.flightbooking.model.Booking;
import com.ab.flightbooking.util.JsonFileHandler;
import com.fasterxml.jackson.core.type.TypeReference;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JsonBookingRepository implements BookingRepository {

    private final JsonFileHandler fileHandler;
    private final Path filePath;
    private final Map<String, Booking> cache;

    public JsonBookingRepository(JsonFileHandler fileHandler, Path filePath) {
        this.fileHandler = fileHandler;
        this.filePath = filePath;
        List<Booking> bookings = fileHandler.readFromFile(filePath, new TypeReference<List<Booking>>() {});
        this.cache = bookings.stream().collect(Collectors.toMap(Booking::id, Function.identity()));
    }

    @Override
    public List<Booking> findAll() {
        return List.copyOf(cache.values());
    }

    @Override
    public Optional<Booking> findById(String id) {
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public Booking save(Booking booking) {
        cache.put(booking.id(), booking);
        fileHandler.writeToFile(filePath, List.copyOf(cache.values()));
        return booking;
    }
}