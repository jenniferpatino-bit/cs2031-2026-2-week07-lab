package org.week07lab.flight.infrastructure;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.week07lab.flight.domain.Flight;

import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long>, JpaSpecificationExecutor<Flight> {

    boolean existsByFlightNumber(String flightNumber);

    /**
     * Bloquea la fila del vuelo mientras se reserva, para que dos reservas simultaneas
     * no puedan tomar el mismo ultimo asiento.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select f from Flight f where f.id = :id")
    Optional<Flight> findByIdForUpdate(Long id);
}
