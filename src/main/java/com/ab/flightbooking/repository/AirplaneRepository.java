package com.ab.flightbooking.repository;

import com.ab.flightbooking.model.Airplane;

import java.util.List;
import java.util.Optional;

public interface AirplaneRepository {
    List<Airplane> findAll();
    Optional<Airplane> findById(String id);
}