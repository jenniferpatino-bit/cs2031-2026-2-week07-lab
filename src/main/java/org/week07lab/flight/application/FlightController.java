package org.week07lab.flight.application;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.week07lab.flight.domain.FlightService;
import org.week07lab.flight.dto.CreateFlightRequest;
import org.week07lab.flight.dto.FlightResponse;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping("/create")
    public ResponseEntity<FlightResponse> create(@Valid @RequestBody CreateFlightRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(flightService.create(request));
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightResponse>> search(
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String airline,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTo) {
        return ResponseEntity.ok(flightService.search(flightNumber, airline, departureFrom, departureTo));
    }
}
