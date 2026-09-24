package org.week07lab.account.infrastructure;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.week07lab.account.domain.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    /**
     * Bloquea la cuenta durante una reserva para que dos reservas simultaneas del mismo
     * usuario no se salten la validacion de conflicto de horario.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.id = :id")
    Optional<Account> findByIdForUpdate(Long id);
}
