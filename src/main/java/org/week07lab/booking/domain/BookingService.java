package org.week07lab.booking.domain;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.week07lab.account.domain.Account;
import org.week07lab.account.domain.Role;
import org.week07lab.account.infrastructure.AccountRepository;
import org.week07lab.booking.dto.BookingResponse;
import org.week07lab.booking.event.BookingConfirmedEvent;
import org.week07lab.booking.infrastructure.BookingRepository;
import org.week07lab.exception.BadRequestException;
import org.week07lab.exception.ConflictException;
import org.week07lab.exception.NotFoundException;
import org.week07lab.flight.domain.Flight;
import org.week07lab.flight.infrastructure.FlightRepository;

import java.time.LocalDateTime;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final AccountRepository accountRepository;
    private final ApplicationEventPublisher eventPublisher;

    public BookingService(BookingRepository bookingRepository, FlightRepository flightRepository,
                          AccountRepository accountRepository, ApplicationEventPublisher eventPublisher) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
        this.accountRepository = accountRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Reserva un asiento para el usuario autenticado. customerId, nombres y fecha de reserva
     * se calculan aqui; el cliente solo envia el flightId.
     */
    @Transactional
    public BookingResponse book(Long flightId, Account authenticated) {
        Account customer = accountRepository.findByIdForUpdate(authenticated.getId())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        Flight flight = flightRepository.findByIdForUpdate(flightId)
                .orElseThrow(() -> new NotFoundException("No existe el vuelo con id " + flightId));

        LocalDateTime now = LocalDateTime.now();
        if (!flight.getDepartureTime().isAfter(now)) {
            throw new BadRequestException("No se puede reservar un vuelo que ya salio o esta en transito");
        }
        if (bookingRepository.existsOverlappingBooking(customer.getId(), flight.getDepartureTime(), flight.getArrivalTime())) {
            throw new ConflictException("Ya tienes una reserva en un vuelo que se superpone con este horario");
        }
        if (!flight.hasAvailableSeats()) {
            throw new ConflictException("El vuelo " + flight.getFlightNumber() + " no tiene asientos disponibles");
        }

        flight.reserveSeat();
        Booking booking = bookingRepository.save(new Booking(customer, flight, now));

        eventPublisher.publishEvent(new BookingConfirmedEvent(
                booking.getId(),
                booking.getCustomerFirstName(),
                booking.getCustomerLastName(),
                customer.getEmail(),
                flight.getFlightNumber(),
                flight.getAirline(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                booking.getBookingDate()
        ));
        return BookingResponse.from(booking);
    }

    /**
     * Solo el duenio de la reserva (o un ADMIN) puede consultarla.
     */
    @Transactional(readOnly = true)
    public BookingResponse getById(Long id, Account authenticated) {
        Booking booking = bookingRepository.findWithDetailsById(id)
                .orElseThrow(() -> new NotFoundException("No existe la reserva con id " + id));
        boolean isOwner = booking.getCustomer().getId().equals(authenticated.getId());
        if (!isOwner && authenticated.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("No tienes permiso para ver esta reserva");
        }
        return BookingResponse.from(booking);
    }
}
