package org.week07lab.flight.dto;

import org.week07lab.flight.domain.Flight;

import java.time.LocalDateTime;

public record FlightResponse(
        Long id,
        String flightNumber,
        String airline,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        Integer availableSeats
) {
    public static FlightResponse from(Flight flight) {
        return new FlightResponse(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getAirline(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getAvailableSeats()
        );
    }
}
