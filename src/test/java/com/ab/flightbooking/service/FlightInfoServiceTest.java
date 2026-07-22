package com.ab.flightbooking.service;

import com.ab.flightbooking.exception.AirportNotFoundException;
import com.ab.flightbooking.model.Airplane;
import com.ab.flightbooking.model.Airport;
import com.ab.flightbooking.model.Flight;
import com.ab.flightbooking.repository.AirplaneRepository;
import com.ab.flightbooking.repository.AirportRepository;
import com.ab.flightbooking.repository.FlightRepository;
import com.ab.flightbooking.service.dto.FlightInfoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightInfoServiceTest {

    @Mock
    private AirportRepository airportRepository;
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private AirplaneRepository airplaneRepository;

    private FlightInfoService flightInfoService;

    private final Airport jfk = new Airport("AP01", "John F. Kennedy International Airport", "New York");
    private final Airport heathrow = new Airport("AP02", "Heathrow Airport", "London");
    private final Airplane boeing737 = new Airplane("A001", "Boeing 737", 189, 26020.0);

    @BeforeEach
    void setUp() {
        flightInfoService = new FlightInfoService(airportRepository, flightRepository, airplaneRepository);
    }

    @Test
    void getFlightInfoByAirportName_returnsCityAndFlights() {
        Flight flight = new Flight("F001", "A001", "AP01", "AP02",
                LocalDateTime.of(2026, 8, 1, 9, 0),
                LocalDateTime.of(2026, 8, 1, 21, 0));

        when(airportRepository.findByName("JFK")).thenReturn(Optional.of(jfk));
        when(flightRepository.findByOriginAirportId("AP01")).thenReturn(List.of(flight));
        when(airplaneRepository.findById("A001")).thenReturn(Optional.of(boeing737));
        when(airportRepository.findById("AP01")).thenReturn(Optional.of(jfk));
        when(airportRepository.findById("AP02")).thenReturn(Optional.of(heathrow));

        FlightInfoResponse response = flightInfoService.getFlightInfoByAirportName("JFK");

        assertEquals("New York", response.city());
        assertEquals(1, response.flights().size());
        assertEquals("Boeing 737", response.flights().get(0).airplaneType());
        assertEquals("Heathrow Airport", response.flights().get(0).destinationAirportName());
    }

    @Test
    void getFlightInfoByAirportName_throwsWhenAirportUnknown() {
        when(airportRepository.findByName("Unknown Airport")).thenReturn(Optional.empty());

        assertThrows(AirportNotFoundException.class,
                () -> flightInfoService.getFlightInfoByAirportName("Unknown Airport"));
    }
}