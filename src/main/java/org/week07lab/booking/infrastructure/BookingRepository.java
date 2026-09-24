package org.week07lab.booking.infrastructure;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.week07lab.booking.domain.Booking;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Dos intervalos [salida, llegada) se solapan si cada uno empieza antes de que termine el otro.
     */
    @Query("""
            select count(b) > 0 from Booking b
            where b.customer.id = :customerId
              and b.flight.departureTime < :arrival
              and b.flight.arrivalTime > :departure
            """)
    boolean existsOverlappingBooking(Long customerId, LocalDateTime departure, LocalDateTime arrival);

    @EntityGraph(attributePaths = {"flight", "customer"})
    Optional<Booking> findWithDetailsById(Long id);
}
