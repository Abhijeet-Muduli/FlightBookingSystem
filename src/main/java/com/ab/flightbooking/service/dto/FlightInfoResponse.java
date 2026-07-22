package com.ab.flightbooking.service.dto;

import java.time.LocalDateTime;
import java.util.List;

public record FlightInfoResponse(String city, List<FlightDetail> flights) {

    public record FlightDetail(
            String flightId,
            String airplaneType,
            String originAirportName,
            String destinationAirportName,
            LocalDateTime departure,
            LocalDateTime arrival
    ) {}
}