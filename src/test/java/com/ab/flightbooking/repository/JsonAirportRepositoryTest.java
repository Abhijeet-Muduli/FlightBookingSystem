package com.ab.flightbooking.repository;

import com.ab.flightbooking.model.Airport;
import com.ab.flightbooking.util.JsonFileHandler;
import com.ab.flightbooking.util.ObjectMapperProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonAirportRepositoryTest {

    private JsonAirportRepository repository;

    @BeforeEach
    void setUp() {
        JsonFileHandler fileHandler = new JsonFileHandler(ObjectMapperProvider.getMapper());
        
        repository = new JsonAirportRepository(fileHandler, "airports.json");
    }

    @Test
    void findAll_returnsAllSeededAirports() {
        List<Airport> airports = repository.findAll();
        assertEquals(3, airports.size());
    }

    @Test
    void findById_returnsMatchingAirport() {
        Optional<Airport> airport = repository.findById("AP01");
        assertTrue(airport.isPresent());
        assertEquals("New York", airport.get().city());
    }

    @Test
    void findByName_isCaseInsensitive() {
        Optional<Airport> airport = repository.findByName("heathrow airport");
        assertTrue(airport.isPresent());
        assertEquals("London", airport.get().city());
    }

    @Test
    void findById_returnsEmptyForUnknownId() {
        assertTrue(repository.findById("UNKNOWN").isEmpty());
    }
}
