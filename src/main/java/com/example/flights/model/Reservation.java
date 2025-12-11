package com.example.flights.model;

import java.util.Objects;

    public class Reservation {
        private final String customerName;
        private final Flight flight;
        private final int seatsBooked;

        public Reservation(String customerName, Flight flight, int seatsBooked) {
            if (seatsBooked <= 0) throw new IllegalArgumentException("seatsBooked must be > 0");
            this.customerName = Objects.requireNonNull(customerName);
            this.flight = Objects.requireNonNull(flight);
            this.seatsBooked = seatsBooked;
        }

        public String getCustomerName() { return customerName; }
        public Flight getFlight() { return flight; }
        public int getSeatsBooked() { return seatsBooked; }

        @Override
        public String toString() {
            return "Reservation{" +
                    "customerName='" + customerName + '\'' +
                    ", flightNumber='" + flight.getFlightNumber() + '\'' +
                    ", destination='" + flight.getDestination() + '\'' +
                    ", departureTime=" + flight.getDepartureTime() +
                    ", seatsBooked=" + seatsBooked +
                    '}';
        }
    }


