package org.week07lab.booking.event;

import java.time.LocalDateTime;

public record BookingConfirmedEvent(
        Long bookingId,
        String passengerFirstName,
        String passengerLastName,
        String passengerEmail,
        String flightNumber,
        String airline,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        LocalDateTime bookingDate
) {
}
