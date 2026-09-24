package org.week07lab.booking.dto;

import org.week07lab.booking.domain.Booking;

import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        Long customerId,
        String customerFirstName,
        String customerLastName,
        LocalDateTime bookingDate,
        Long flightId,
        String flightNumber,
        String airline,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime
) {
    public static BookingResponse from(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getCustomer().getId(),
                booking.getCustomerFirstName(),
                booking.getCustomerLastName(),
                booking.getBookingDate(),
                booking.getFlight().getId(),
                booking.getFlight().getFlightNumber(),
                booking.getFlight().getAirline(),
                booking.getFlight().getDepartureTime(),
                booking.getFlight().getArrivalTime()
        );
    }
}
