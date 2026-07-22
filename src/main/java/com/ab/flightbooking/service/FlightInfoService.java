package com.ab.flightbooking.service;

import com.ab.flightbooking.exception.AirportNotFoundException;
import com.ab.flightbooking.model.Airplane;
import com.ab.flightbooking.model.Airport;
import com.ab.flightbooking.model.Flight;
import com.ab.flightbooking.repository.AirplaneRepository;
import com.ab.flightbooking.repository.AirportRepository;
import com.ab.flightbooking.repository.FlightRepository;
import com.ab.flightbooking.service.dto.FlightInfoResponse;
import com.ab.flightbooking.service.dto.FlightInfoResponse.FlightDetail;

import java.util.List;

public class FlightInfoService {

    private final AirportRepository airportRepository;
    private final FlightRepository flightRepository;
    private final AirplaneRepository airplaneRepository;

    public FlightInfoService(AirportRepository airportRepository,
                             FlightRepository flightRepository,
                             AirplaneRepository airplaneRepository) {
        this.airportRepository = airportRepository;
        this.flightRepository = flightRepository;
        this.airplaneRepository = airplaneRepository;
    }

    public FlightInfoResponse getFlightInfoByAirportName(String airportName) {
        Airport originAirport = airportRepository.findByName(airportName)
                .orElseThrow(() -> new AirportNotFoundException(airportName));

        List<Flight> departingFlights = flightRepository.findByOriginAirportId(originAirport.id());

        List<FlightDetail> details = departingFlights.stream()
                .map(this::toFlightDetail)
                .toList();

        return new FlightInfoResponse(originAirport.city(), details);
    }

    private FlightDetail toFlightDetail(Flight flight) {
        Airplane airplane = airplaneRepository.findById(flight.airplaneId())
                .orElseThrow(() -> new IllegalStateException("Airplane not found: " + flight.airplaneId()));
        Airport origin = airportRepository.findById(flight.originAirportId())
                .orElseThrow(() -> new IllegalStateException("Origin airport not found: " + flight.originAirportId()));
        Airport destination = airportRepository.findById(flight.destAirportId())
                .orElseThrow(() -> new IllegalStateException("Destination airport not found: " + flight.destAirportId()));

        return new FlightDetail(
                flight.id(),
                airplane.type(),
                origin.name(),
                destination.name(),
                flight.departure(),
                flight.arrival()
        );
    }
}