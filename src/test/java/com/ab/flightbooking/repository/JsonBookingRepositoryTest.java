package com.ab.flightbooking.repository;

import com.ab.flightbooking.model.Booking;
import com.ab.flightbooking.util.JsonFileHandler;
import com.ab.flightbooking.util.ObjectMapperProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonBookingRepositoryTest {

    @TempDir
    Path tempDir;

    private JsonBookingRepository repository;
    private Path bookingsFile;

    @BeforeEach
    void setUp() {
        bookingsFile = tempDir.resolve("bookings.json");
        JsonFileHandler fileHandler = new JsonFileHandler(ObjectMapperProvider.getMapper());
        // File doesn't exist yet - repository should start with an empty cache.
        repository = new JsonBookingRepository(fileHandler, bookingsFile);
    }

    @Test
    void findAll_isEmptyWhenFileDoesNotExist() {
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void save_addsBookingAndPersistsToFile() {
        Booking booking = new Booking("B001", "Abhijeet Muduli", "F001", "CONFIRMED", LocalDateTime.now());

        repository.save(booking);

        assertEquals(1, repository.findAll().size());
        assertTrue(repository.findById("B001").isPresent());
        assertTrue(Files.exists(bookingsFile));
    }
}