package com.ab.flightbooking.model;

public record Airplane(
        String id,
        String type,
        int totalCapacity,
        double fuelCapacity
) {}