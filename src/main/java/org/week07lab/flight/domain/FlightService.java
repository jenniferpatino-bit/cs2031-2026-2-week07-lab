package org.week07lab.flight.domain;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.week07lab.exception.BadRequestException;
import org.week07lab.exception.ConflictException;
import org.week07lab.flight.dto.CreateFlightRequest;
import org.week07lab.flight.dto.FlightResponse;
import org.week07lab.flight.infrastructure.FlightRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.week07lab.flight.infrastructure.FlightSpecifications.*;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Transactional
    public FlightResponse create(CreateFlightRequest request) {
        if (!request.departureTime().isBefore(request.arrivalTime())) {
            throw new BadRequestException("La hora de salida debe ser anterior a la hora de llegada");
        }
        if (flightRepository.existsByFlightNumber(request.flightNumber())) {
            throw new ConflictException("Ya existe un vuelo con el numero " + request.flightNumber());
        }
        Flight flight = flightRepository.save(new Flight(
                request.flightNumber(),
                request.airline().trim(),
                request.departureTime(),
                request.arrivalTime(),
                request.availableSeats()
        ));
        return FlightResponse.from(flight);
    }

    @Transactional(readOnly = true)
    public List<FlightResponse> search(String flightNumber, String airline,
                                       LocalDateTime departureFrom, LocalDateTime departureTo) {
        if (departureFrom != null && departureTo != null && departureFrom.isAfter(departureTo)) {
            throw new BadRequestException("departureFrom debe ser anterior o igual a departureTo");
        }
        Specification<Flight> spec = Specification.allOf(
                flightNumberContains(flightNumber),
                airlineContains(airline),
                departureFrom(departureFrom),
                departureTo(departureTo)
        );
        return flightRepository.findAll(spec, Sort.by("departureTime")).stream()
                .map(FlightResponse::from)
                .toList();
    }
}
