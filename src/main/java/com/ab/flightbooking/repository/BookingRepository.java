package com.ab.flightbooking.repository;

import com.ab.flightbooking.model.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    List<Booking> findAll();
    Optional<Booking> findById(String id);
    Booking save(Booking booking);
}