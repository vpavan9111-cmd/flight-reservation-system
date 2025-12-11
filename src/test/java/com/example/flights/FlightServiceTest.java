package com.example.flights;

import com.example.flights.model.Flight;
import com.example.flights.model.Reservation;
import com.example.flights.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

    class FlightServiceTest {
        private FlightService service;
        private Flight f1;

        @BeforeEach
        void setUp() {
            service = new FlightService();
            f1 = new Flight("AA101", "Chicago", LocalDateTime.of(2025,12,15,9,30), 3);
            service.addFlight(f1);
        }

        @Test
        void searchFlights_found() {
            List<Flight> r = service.searchFlights("Chicago", LocalDateTime.of(2025,12,15,0,0));
            assertFalse(r.isEmpty());
            assertEquals("AA101", r.get(0).getFlightNumber());
        }

        @Test
        void searchFlights_noMatch() {
            List<Flight> r = service.searchFlights("Boston", LocalDateTime.of(2025,12,15,0,0));
            assertTrue(r.isEmpty());
        }

        @Test
        void bookFlight_success() {
            Reservation res = service.bookFlight("Alice", f1, 2);
            assertNotNull(res);
            assertEquals(1, f1.getAvailableSeats());
        }

        @Test
        void bookFlight_overbook_shouldThrow() {
            IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
                service.bookFlight("Bob", f1, 5);
            });
            assertTrue(ex.getMessage().contains("Not enough seats"));
        }

        @Test
        void getReservationsForCustomer() {
            service.bookFlight("Alice", f1, 1);
            service.bookFlight("Alice", f1, 1);
            assertEquals(2, service.getReservationsForCustomer("Alice").size());
        }
    }



