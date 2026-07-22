package com.ab.flightbooking.repository;

import com.ab.flightbooking.model.Airport;

import java.util.List;
import java.util.Optional;

public interface AirportRepository {
    List<Airport> findAll();
    Optional<Airport> findById(String id);
    Optional<Airport> findByName(String name);
}