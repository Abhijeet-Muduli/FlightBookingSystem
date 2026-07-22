# FlightBookingSystem

A file-based (JSON) flight booking system built in Java with Maven — a layered, service-oriented CLI application for practicing real-world Java design patterns: models, repositories, services, DTOs, and custom exceptions.

##Tech Stack:
Concern	Library
Language	Java 17 (uses records)
Build tool	Maven
JSON serialization	Jackson (jackson-databind + jackson-datatype-jsr310 for LocalDateTime)
Logging	SLF4J (slf4j-api + slf4j-simple)
Testing	JUnit 5

##Prerequisites:
java -version   # 25 
mvn -version    # any recent Maven 3.x

##Folder Structure:
flight-booking/
├── pom.xml
├── data/                          # live data — read AND written at runtime
│   ├── flights.json
│   └── bookings.json
├── src/
│   ├── main/
│   │   ├── java/com/ab/flightbooking/
│   │   │   ├── App.java                    # CLI entry point
│   │   │   ├── model/                      # Airplane, Airport, Flight, Booking
│   │   │   ├── repository/                 # interfaces + JSON-backed impls
│   │   │   ├── service/                    # FlightInfoService, BookingService
│   │   │   │   └── dto/                    # FlightInfoResponse, BookingResult
│   │   │   ├── exception/                  # custom unchecked exceptions
│   │   │   └── util/                       # JsonFileHandler, ObjectMapperProvider
│   │   └── resources/                      # static seed data — read only
│   │       ├── airports.json
│   │       └── airplanes.json
│   └── test/java/com/ab/flightbooking/
│       ├── repository/                     # file I/O tests
│       └── service/                        # Mockito-based unit tests

##Commands (through terminal):
Task:	                            Command:
Compile	                          mvn compile
Run the CLI	                      mvn compile exec:java
Run tests        	                mvn test
Build a runnable JAR             	mvn package → target/flight-booking.jar
Run the JAR	                      java -jar target/flight-booking.jar
Clean build + test + package	    mvn clean compile test package

How to Use It:
On launch, the app loads all seed data into memory and shows a menu that loops until you exit.
===== Flight Booking System =====
1. Search flights by airport name
2. Book a flight
3. Exit

Option 1 — Search Flights by Airport

You provide: the full airport name (exact match, case-insensitive)

The app returns:
The city that airport is located in
Every flight departing from it, showing: flight ID, route, airplane type, departure time, arrival time

If the name doesn't match anything seeded, you'll get Error: No airport found with name: ... and return to the menu — no crash.

Option 2 — Book a Flight

You provide (in order): your name → departure city → flight ID (from Option 1's results)

The app validates that the chosen flight actually departs from the city you typed, then creates and permanently saves the booking to data/bookings.json.

Possible errors instead of a confirmation:

No flight found with id: ... — flight ID doesn't exist
Flight ... does not depart from ... (it departs from ...) — city/flight mismatch
Option 3 — Exit

Prints Goodbye! and terminates. No confirmation prompt.

##Running the Tests:

command: mvn test

Covers:

JsonAirportRepositoryTest — reads real seeded classpath data
JsonFlightRepositoryTest — file I/O against a @TempDir, including save() persistence
JsonBookingRepositoryTest — empty-file handling, save + persist
FlightInfoServiceTest — Mockito-mocked repositories, happy path + not-found case
BookingServiceTest — Mockito-mocked repositories, happy path + not-found + city-mismatch cases
