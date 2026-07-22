package com.ab.flightbooking.repository;

import com.ab.flightbooking.model.Flight;
import com.ab.flightbooking.util.JsonFileHandler;
import com.ab.flightbooking.util.ObjectMapperProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonFlightRepositoryTest {

    @TempDir
    Path tempDir;

    private Path flightsFile;
    private JsonFlightRepository repository;

    @BeforeEach
    void setUp() throws IOException {
        flightsFile = tempDir.resolve("flights.json");
        String seedJson = """
                [
                  { "id": "F001", "airplaneId": "A001", "originAirportId": "AP01",
                    "destAirportId": "AP02", "departure": "2026-08-01T09:00:00",
                    "arrival": "2026-08-01T21:00:00" }
                ]
                """;
        Files.writeString(flightsFile, seedJson);

        JsonFileHandler fileHandler = new JsonFileHandler(ObjectMapperProvider.getMapper());
        repository = new JsonFlightRepository(fileHandler, flightsFile);
    }

    @Test
    void findById_returnsSeededFlight() {
        Optional<Flight> flight = repository.findById("F001");
        assertTrue(flight.isPresent());
        assertEquals("A001", flight.get().airplaneId());
    }

    @Test
    void findByOriginAirportId_filtersCorrectly() {
        List<Flight> flights = repository.findByOriginAirportId("AP01");
        assertEquals(1, flights.size());
        assertTrue(repository.findByOriginAirportId("AP99").isEmpty());
    }

    @Test
    void save_persistsNewFlightToDisk() throws IOException {
        Flight newFlight = new Flight("F999", "A002", "AP02", "AP03",
                LocalDateTime.of(2026, 9, 1, 10, 0),
                LocalDateTime.of(2026, 9, 1, 14, 0));

        repository.save(newFlight);

        assertTrue(repository.findById("F999").isPresent());
        String fileContent = Files.readString(flightsFile);
        assertTrue(fileContent.contains("F999"));
    }
}