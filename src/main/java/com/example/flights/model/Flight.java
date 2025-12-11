package com.example.flights.model;

import java.time.LocalDateTime;
import java.util.Objects;

    public class Flight {
        private final String flightNumber;
        private final String destination;
        private final LocalDateTime departureTime;
        private int availableSeats;

        public Flight(String flightNumber, String destination, LocalDateTime departureTime, int availableSeats) {
            if (availableSeats < 0) throw new IllegalArgumentException("availableSeats cannot be negative");
            this.flightNumber = Objects.requireNonNull(flightNumber);
            this.destination = Objects.requireNonNull(destination);
            this.departureTime = Objects.requireNonNull(departureTime);
            this.availableSeats = availableSeats;
        }

        public String getFlightNumber() { return flightNumber; }
        public String getDestination() { return destination; }
        public LocalDateTime getDepartureTime() { return departureTime; }
        public int getAvailableSeats() { return availableSeats; }

        public synchronized boolean reserveSeats(int seats) {
            if (seats <= 0) return false;
            if (availableSeats - seats < 0) return false;
            availableSeats -= seats;
            return true;
        }

        public synchronized void releaseSeats(int seats) {
            if (seats > 0) availableSeats += seats;
        }

        @Override
        public String toString() {
            return "Flight{" +
                    "flightNumber='" + flightNumber + '\'' +
                    ", destination='" + destination + '\'' +
                    ", departureTime=" + departureTime +
                    ", availableSeats=" + availableSeats +
                    '}';
        }
    }


