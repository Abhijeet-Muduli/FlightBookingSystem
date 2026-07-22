package com.ab.flightbooking.exception;

public class AirportNotFoundException extends RuntimeException {
    public AirportNotFoundException(String airportName) {
        super("No airport found with name: " + airportName);
    }
}