package com.ab.flightbooking.model;
import java.time.LocalDateTime;
public record Flight(
        String id,
        String airplaneId,
        String originAirportId,
        String destAirportId,
        LocalDateTime departure,
        LocalDateTime arrival
) {}