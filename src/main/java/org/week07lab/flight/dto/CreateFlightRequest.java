package org.week07lab.flight.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CreateFlightRequest(
        @NotBlank(message = "El numero de vuelo es obligatorio")
        @Pattern(regexp = "^[A-Z0-9]{1,6}$", message = "El numero de vuelo solo admite A-Z y 0-9, maximo 6 caracteres")
        String flightNumber,

        @NotBlank(message = "La aerolinea es obligatoria")
        String airline,

        @NotNull(message = "La hora de salida es obligatoria")
        LocalDateTime departureTime,

        @NotNull(message = "La hora de llegada es obligatoria")
        LocalDateTime arrivalTime,

        @NotNull(message = "Los asientos disponibles son obligatorios")
        @Positive(message = "Los asientos disponibles deben ser mayores a 0")
        Integer availableSeats
) {
}
