package org.week07lab.flight.infrastructure;

import org.springframework.data.jpa.domain.Specification;
import org.week07lab.flight.domain.Flight;

import java.time.LocalDateTime;

/**
 * Filtros opcionales para la busqueda de vuelos: cada uno se aplica solo si viene informado.
 */
public final class FlightSpecifications {

    private FlightSpecifications() {
    }

    public static Specification<Flight> flightNumberContains(String flightNumber) {
        return (root, query, cb) -> flightNumber == null || flightNumber.isBlank()
                ? null
                : cb.like(cb.upper(root.get("flightNumber")), "%" + escape(flightNumber.trim().toUpperCase()) + "%", '\\');
    }

    public static Specification<Flight> airlineContains(String airline) {
        return (root, query, cb) -> airline == null || airline.isBlank()
                ? null
                : cb.like(cb.lower(root.get("airline")), "%" + escape(airline.trim().toLowerCase()) + "%", '\\');
    }

    public static Specification<Flight> departureFrom(LocalDateTime from) {
        return (root, query, cb) -> from == null ? null : cb.greaterThanOrEqualTo(root.get("departureTime"), from);
    }

    public static Specification<Flight> departureTo(LocalDateTime to) {
        return (root, query, cb) -> to == null ? null : cb.lessThanOrEqualTo(root.get("departureTime"), to);
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }
}
