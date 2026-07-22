package com.ab.flightbooking.repository;

import com.ab.flightbooking.model.Flight;

import java.util.List;
import java.util.Optional;

public interface FlightRepository {
    List<Flight> findAll();
    Optional<Flight> findById(String id);
    List<Flight> findByOriginAirportId(String airportId);
}