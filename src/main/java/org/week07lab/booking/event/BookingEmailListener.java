package org.week07lab.booking.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

/**
 * Genera el "email" de confirmacion como archivo flight_booking_email_{bookingId}.txt.
 * Se ejecuta solo despues del commit, para no generar emails de reservas que hicieron rollback.
 */
@Component
public class BookingEmailListener {

    private static final Logger log = LoggerFactory.getLogger(BookingEmailListener.class);
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final Path emailDir;

    public BookingEmailListener(@Value("${app.booking.email-dir}") String emailDir) {
        this.emailDir = Path.of(emailDir);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onBookingConfirmed(BookingConfirmedEvent event) {
        Path file = emailDir.resolve("flight_booking_email_" + event.bookingId() + ".txt");
        try {
            Files.createDirectories(emailDir);
            Files.writeString(file, render(event), StandardCharsets.UTF_8);
            log.info("Email de confirmacion generado en {}", file.toAbsolutePath());
        } catch (IOException e) {
            log.error("No se pudo generar el email de confirmacion {}", file.toAbsolutePath(), e);
        }
    }

    private String render(BookingConfirmedEvent event) {
        return """
                To: %s
                Subject: Confirmacion de reserva #%d - Vuelo %s

                Hola %s %s,

                Tu reserva ha sido confirmada.

                Pasajero:        %s %s
                Numero de vuelo: %s
                Aerolinea:       %s
                Salida:          %s
                Llegada:         %s
                Fecha de reserva: %s

                Gracias por volar con Fly Away Travel.
                """.formatted(
                event.passengerEmail(),
                event.bookingId(), event.flightNumber(),
                event.passengerFirstName(), event.passengerLastName(),
                event.passengerFirstName(), event.passengerLastName(),
                event.flightNumber(),
                event.airline(),
                ISO.format(event.departureTime()),
                ISO.format(event.arrivalTime()),
                ISO.format(event.bookingDate())
        );
    }
}
