package com.ab.flightbooking.service.dto;

import com.ab.flightbooking.model.Booking;
import com.ab.flightbooking.model.Flight;

public record BookingResult(String message, Flight flight, Booking booking) {}