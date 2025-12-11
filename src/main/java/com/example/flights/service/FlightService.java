package com.example.flights.service;

import com.example.flights.model.Flight;
import com.example.flights.model.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

    public class FlightService {
        private final List<Flight> flights = new ArrayList<>();
        private final List<Reservation> reservations = new ArrayList<>();

        public FlightService() { }

        // For tests / init: add flight
        public void addFlight(Flight flight) {
            flights.add(Objects.requireNonNull(flight));
        }

        /**
         * Search flights to a destination on the same date (ignores time-of-day).
         */
        public List<Flight> searchFlights(String destination, LocalDateTime date) {
            Objects.requireNonNull(destination);
            Objects.requireNonNull(date);
            LocalDate target = date.toLocalDate();
            return flights.stream()
                    .filter(f -> f.getDestination().equalsIgnoreCase(destination.trim()))
                    .filter(f -> f.getDepartureTime().toLocalDate().equals(target))
                    .filter(f -> f.getAvailableSeats() > 0)
                    .sorted(Comparator.comparing(Flight::getDepartureTime))
                    .collect(Collectors.toList());
        }

        /**
         * Attempt to book seats. Returns a Reservation if successful, otherwise throws an exception.
         */
        public synchronized Reservation bookFlight(String customerName, Flight flight, int seats) {
            Objects.requireNonNull(customerName);
            Objects.requireNonNull(flight);
            if (seats <= 0) throw new IllegalArgumentException("Seats must be positive");
            // ensure flight is managed by this service
            Optional<Flight> managed = flights.stream()
                    .filter(f -> f.getFlightNumber().equals(flight.getFlightNumber()))
                    .findFirst();
            if (managed.isEmpty()) throw new IllegalArgumentException("Flight not found in system");
            Flight managedFlight = managed.get();

            boolean reserved = managedFlight.reserveSeats(seats);
            if (!reserved) {
                throw new IllegalStateException("Not enough seats available");
            }
            Reservation r = new Reservation(customerName, managedFlight, seats);
            reservations.add(r);
            return r;
        }

        public List<Reservation> getReservationsForCustomer(String customerName) {
            return reservations.stream()
                    .filter(r -> r.getCustomerName().equalsIgnoreCase(customerName.trim()))
                    .collect(Collectors.toList());
        }

        // helper getters for tests / UI
        public List<Flight> getAllFlights() { return Collections.unmodifiableList(flights); }
        public List<Reservation> getAllReservations() { return Collections.unmodifiableList(reservations); }
    }


