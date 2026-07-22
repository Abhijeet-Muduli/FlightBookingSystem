package com.ab.flightbooking.service;

import com.ab.flightbooking.exception.BookingException;
import com.ab.flightbooking.exception.FlightNotFoundException;
import com.ab.flightbooking.model.Airport;
import com.ab.flightbooking.model.Booking;
import com.ab.flightbooking.model.Flight;
import com.ab.flightbooking.repository.AirportRepository;
import com.ab.flightbooking.repository.BookingRepository;
import com.ab.flightbooking.repository.FlightRepository;
import com.ab.flightbooking.service.dto.BookingResult;

import java.time.LocalDateTime;
import java.util.UUID;

public class BookingService {

    private final AirportRepository airportRepository;
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;

    public BookingService(AirportRepository airportRepository,
                          FlightRepository flightRepository,
                          BookingRepository bookingRepository) {
        this.airportRepository = airportRepository;
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
    }

    public BookingResult bookFlight(String customerName, String city, String flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException(flightId));

        Airport originAirport = airportRepository.findById(flight.originAirportId())
                .orElseThrow(() -> new IllegalStateException("Origin airport not found: " + flight.originAirportId()));

        if (!originAirport.city().equalsIgnoreCase(city)) {
            throw new BookingException(
                    "Flight " + flightId + " does not depart from " + city
                            + " (it departs from " + originAirport.city() + ")");
        }

        Booking booking = new Booking(
                UUID.randomUUID().toString(),
                customerName,
                flightId,
                "CONFIRMED",
                LocalDateTime.now()
        );

        bookingRepository.save(booking);

        return new BookingResult("Confirmed", flight, booking);
    }
}