package com.ab.flightbooking.exception;

public class FlightNotFoundException extends RuntimeException {
    public FlightNotFoundException(String flightId) {
        super("No flight found with id: " + flightId);
    }
}