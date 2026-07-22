package com.ab.flightbooking.service;

import com.ab.flightbooking.exception.BookingException;
import com.ab.flightbooking.exception.FlightNotFoundException;
import com.ab.flightbooking.model.Airport;
import com.ab.flightbooking.model.Flight;
import com.ab.flightbooking.repository.AirportRepository;
import com.ab.flightbooking.repository.BookingRepository;
import com.ab.flightbooking.repository.FlightRepository;
import com.ab.flightbooking.service.dto.BookingResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private AirportRepository airportRepository;
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private BookingRepository bookingRepository;

    private BookingService bookingService;

    private final Flight flight = new Flight("F001", "A001", "AP01", "AP02",
            LocalDateTime.of(2026, 8, 1, 9, 0),
            LocalDateTime.of(2026, 8, 1, 21, 0));
    private final Airport jfk = new Airport("AP01", "John F. Kennedy International Airport", "New York");

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(airportRepository, flightRepository, bookingRepository);
    }

    @Test
    void bookFlight_createsConfirmedBooking() {
        when(flightRepository.findById("F001")).thenReturn(Optional.of(flight));
        when(airportRepository.findById("AP01")).thenReturn(Optional.of(jfk));

        BookingResult result = bookingService.bookFlight("Abhijeet Muduli", "New York", "F001");

        assertEquals("Confirmed", result.message());
        assertEquals("Abhijeet Muduli", result.booking().customerName());
        assertEquals("CONFIRMED", result.booking().status());
        verify(bookingRepository).save(any());
    }

    @Test
    void bookFlight_throwsWhenFlightUnknown() {
        when(flightRepository.findById("F999")).thenReturn(Optional.empty());

        assertThrows(FlightNotFoundException.class,
                () -> bookingService.bookFlight("Abhijeet Muduli", "New York", "F999"));
    }

    @Test
    void bookFlight_throwsWhenCityDoesNotMatchFlightOrigin() {
        when(flightRepository.findById("F001")).thenReturn(Optional.of(flight));
        when(airportRepository.findById("AP01")).thenReturn(Optional.of(jfk));

        assertThrows(BookingException.class,
                () -> bookingService.bookFlight("Abhijeet Muduli", "London", "F001"));
    }
}