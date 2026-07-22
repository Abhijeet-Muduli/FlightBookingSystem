package com.ab.flightbooking;

import com.ab.flightbooking.exception.AirportNotFoundException;
import com.ab.flightbooking.exception.BookingException;
import com.ab.flightbooking.exception.FlightNotFoundException;
import com.ab.flightbooking.repository.AirplaneRepository;
import com.ab.flightbooking.repository.AirportRepository;
import com.ab.flightbooking.repository.BookingRepository;
import com.ab.flightbooking.repository.FlightRepository;
import com.ab.flightbooking.repository.JsonAirplaneRepository;
import com.ab.flightbooking.repository.JsonAirportRepository;
import com.ab.flightbooking.repository.JsonBookingRepository;
import com.ab.flightbooking.repository.JsonFlightRepository;
import com.ab.flightbooking.service.BookingService;
import com.ab.flightbooking.service.FlightInfoService;
import com.ab.flightbooking.service.dto.BookingResult;
import com.ab.flightbooking.service.dto.FlightInfoResponse;
import com.ab.flightbooking.service.dto.FlightInfoResponse.FlightDetail;
import com.ab.flightbooking.util.JsonFileHandler;
import com.ab.flightbooking.util.ObjectMapperProvider;

import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class App {

    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

    public static void main(String[] args) {
        JsonFileHandler fileHandler = new JsonFileHandler(ObjectMapperProvider.getMapper());

        // Static seed data - read from the classpath (src/main/resources)
        AirportRepository airportRepository = new JsonAirportRepository(fileHandler, "airports.json");
        AirplaneRepository airplaneRepository = new JsonAirplaneRepository(fileHandler, "airplanes.json");

        // Live data - read/written from the /data folder on disk
        FlightRepository flightRepository = new JsonFlightRepository(fileHandler, Path.of("data", "flights.json"));
        BookingRepository bookingRepository = new JsonBookingRepository(fileHandler, Path.of("data", "bookings.json"));

        FlightInfoService flightInfoService = new FlightInfoService(airportRepository, flightRepository, airplaneRepository);
        BookingService bookingService = new BookingService(airportRepository, flightRepository, bookingRepository);

        try (Scanner scanner = new Scanner(System.in)) {
            runMenu(scanner, flightInfoService, bookingService);
        }
    }

    private static void runMenu(Scanner scanner, FlightInfoService flightInfoService, BookingService bookingService) {
        while (true) {
            System.out.println("\n===== Flight Booking System =====");
            System.out.println("1. Search flights by airport name");
            System.out.println("2. Book a flight");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> handleFlightSearch(scanner, flightInfoService);
                case "2" -> handleBooking(scanner, bookingService);
                case "3" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option, please choose 1, 2 or 3.");
            }
        }
    }

    private static void handleFlightSearch(Scanner scanner, FlightInfoService flightInfoService) {
        System.out.print("Enter airport name: ");
        String airportName = scanner.nextLine().trim();

        try {
            FlightInfoResponse response = flightInfoService.getFlightInfoByAirportName(airportName);
            System.out.println("\nCity: " + response.city());

            List<FlightDetail> flights = response.flights();
            if (flights.isEmpty()) {
                System.out.println("No flights currently scheduled from this airport.");
                return;
            }

            System.out.println("Flights departing from this airport:");
            for (FlightDetail f : flights) {
                System.out.printf(
                        "  [%s] %s -> %s | Airplane: %s | Departs: %s | Arrives: %s%n",
                        f.flightId(),
                        f.originAirportName(),
                        f.destinationAirportName(),
                        f.airplaneType(),
                        f.departure().format(DISPLAY_FORMAT),
                        f.arrival().format(DISPLAY_FORMAT)
                );
            }
        } catch (AirportNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleBooking(Scanner scanner, BookingService bookingService) {
        System.out.print("Enter your name: ");
        String customerName = scanner.nextLine().trim();

        System.out.print("Enter departure city: ");
        String city = scanner.nextLine().trim();

        System.out.print("Enter flight ID: ");
        String flightId = scanner.nextLine().trim();

        try {
            BookingResult result = bookingService.bookFlight(customerName, city, flightId);
            System.out.println("\n" + result.message() + "!");
            System.out.println("Booking ID: " + result.booking().id());
            System.out.println("Passenger: " + result.booking().customerName());
            System.out.println("Flight: " + result.flight().id()
                    + " | Departs: " + result.flight().departure().format(DISPLAY_FORMAT)
                    + " | Arrives: " + result.flight().arrival().format(DISPLAY_FORMAT));
        } catch (FlightNotFoundException | BookingException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}