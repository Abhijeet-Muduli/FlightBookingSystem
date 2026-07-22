package com.ab.flightbooking.model;

import java.time.LocalDateTime;

public record Booking(
        String id,
        String customerName,
        String flightId,
        String status,
        LocalDateTime bookedAt
) {}