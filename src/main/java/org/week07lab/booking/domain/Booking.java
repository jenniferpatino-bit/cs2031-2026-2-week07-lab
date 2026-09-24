package org.week07lab.booking.domain;

import jakarta.persistence.*;
import org.week07lab.account.domain.Account;
import org.week07lab.flight.domain.Flight;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Account customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @Column(nullable = false)
    private String customerFirstName;

    @Column(nullable = false)
    private String customerLastName;

    @Column(nullable = false)
    private LocalDateTime bookingDate;

    protected Booking() {
    }

    public Booking(Account customer, Flight flight, LocalDateTime bookingDate) {
        this.customer = customer;
        this.flight = flight;
        this.customerFirstName = customer.getFirstName();
        this.customerLastName = customer.getLastName();
        this.bookingDate = bookingDate;
    }

    public Long getId() {
        return id;
    }

    public Account getCustomer() {
        return customer;
    }

    public Flight getFlight() {
        return flight;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }
}
