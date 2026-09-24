package org.week07lab.booking.application;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.week07lab.account.domain.Account;
import org.week07lab.booking.domain.BookingService;
import org.week07lab.booking.dto.BookFlightRequest;
import org.week07lab.booking.dto.BookingResponse;

@RestController
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/flights/book")
    public ResponseEntity<BookingResponse> book(@Valid @RequestBody BookFlightRequest request,
                                                @AuthenticationPrincipal Account account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.book(request.flightId(), account));
    }

    @GetMapping("/flight/book/{id}")
    public ResponseEntity<BookingResponse> getById(@PathVariable Long id, @AuthenticationPrincipal Account account) {
        return ResponseEntity.ok(bookingService.getById(id, account));
    }
}
