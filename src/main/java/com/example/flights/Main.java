package com.example.flights;

import com.example.flights.model.Flight;
import com.example.flights.model.Reservation;
import com.example.flights.service.FlightService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

    public class Main {
        private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        public static void main(String[] args) {
            FlightService service = new FlightService();
            seedData(service);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to Simple Flight Reservation System");

            wgit checkout --orphan newbranchhile (true) {
                System.out.println("\nChoose: 1) Search flights  2) Book flight  3) View my reservations  4) List all flights  5) Exit");
                System.out.print("> ");
                String choice = scanner.nextLine().trim();
                try {
                    switch (choice) {
                        case "1" -> doSearch(scanner, service);
                        case "2" -> doBook(scanner, service);
                        case "3" -> doViewReservations(scanner, service);
                        case "4" -> listAllFlights(service);
                        case "5" -> { System.out.println("Goodbye."); scanner.close(); return; }
                        default -> System.out.println("Invalid choice.");
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }

        private static void seedData(FlightService svc) {
            svc.addFlight(new Flight("AA101", "Chicago", LocalDateTime.parse("2025-12-15 09:30", DTF), 5));
            svc.addFlight(new Flight("AA102", "Chicago", LocalDateTime.parse("2025-12-15 14:00", DTF), 2));
            svc.addFlight(new Flight("DL200", "New York", LocalDateTime.parse("2025-12-15 08:00", DTF), 10));
            svc.addFlight(new Flight("UA300", "Los Angeles", LocalDateTime.parse("2025-12-16 07:45", DTF), 8));
        }

        private static void doSearch(Scanner scanner, FlightService svc) {
            System.out.print("Destination: ");
            String dest = scanner.nextLine().trim();
            System.out.print("Date (yyyy-MM-dd): ");
            String dateStr = scanner.nextLine().trim();
            LocalDateTime date = LocalDateTime.parse(dateStr + " 00:00", DTF);
            List<Flight> results = svc.searchFlights(dest, date);
            if (results.isEmpty()) {
                System.out.println("No flights found.");
            } else {
                System.out.println("Available flights:");
                results.forEach(f -> System.out.println(f.getFlightNumber() + " | " +
                        f.getDepartureTime().format(DTF) + " | seats: " + f.getAvailableSeats()));
            }
        }

        private static void doBook(Scanner scanner, FlightService svc) {
            System.out.print("Your name: ");
            String name = scanner.nextLine().trim();


            if (!name.matches("[a-zA-Z]+")) {
                System.out.println("Invalid name. Name should contain only alphabetic characters (A–Z).");
                return;
            }

            System.out.print("Flight number to book: ");
            String num = scanner.nextLine().trim();

            Flight flight = svc.getAllFlights().stream()
                    .filter(f -> f.getFlightNumber().equalsIgnoreCase(num))
                    .findFirst()
                    .orElse(null);

            if (flight == null) {
                System.out.println("Flight not found.");
                return;
            }

            System.out.print("Seats to book: ");
            String seatInput = scanner.nextLine().trim();

            if (!seatInput.matches("\\d+")) {
                System.out.println("Invalid input. Seats must be a positive integer (e.g., 1, 2, 3).");
                return;
            }

            int seats = Integer.parseInt(seatInput);

            if (seats <= 0) {
                System.out.println("Seats must be greater than zero.");
                return;
            }

            try {
                Reservation r = svc.bookFlight(name, flight, seats);
                System.out.println("Booked: " + r);
            } catch (Exception e) {
                System.out.println("Booking failed: " + e.getMessage());
            }
        }


        private static void doViewReservations(Scanner scanner, FlightService svc) {
            System.out.print("Your name: ");
            String name = scanner.nextLine().trim();
            List<Reservation> res = svc.getReservationsForCustomer(name);
            if (res.isEmpty()) System.out.println("No reservations found for " + name);
            else res.forEach(System.out::println);
        }

        private static void listAllFlights(FlightService svc) {
            svc.getAllFlights().forEach(System.out::println);
        }
    }






