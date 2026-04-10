package com.example.ticketService.repositories;

import com.example.ticketService.entities.Ticket;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

//TODO проверить запросы на *
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Получение событий билетов по статусу и ID события
     * */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Ticket> findByEventEventIdAndStatus(Long eventId, String status);

    @Lock(LockModeType.PESSIMISTIC_WRITE) // Добавляет SELECT ... FOR UPDATE
    @Query("SELECT t FROM Ticket t WHERE t.event.eventId = :eventId " +
            "AND t.status = :status")
    List<Ticket> findAndLockAvailableTickets(
            @Param("eventId") Long eventId,
            @Param("status") String status,
            Pageable pageable);

    /**
     * Получение событий билетов по статусу и ID события с лимитом
     * */
    @Query("""
    SELECT t
    FROM Ticket t
    WHERE t.event.eventId = :eventId
      AND t.status = :status
""")
    List<Ticket> findByEventEventIdAndStatus(
            Long eventId,
            String status,
            Pageable pageable
    );

    /**
     * Получение билетов по ID покупки
     * */
    List<Ticket> findByPurchasePurchaseId(Long id);

    /**
     * Кол-во купленных билетов
     * */
    @Query("""
       SELECT COUNT(t)
       FROM Ticket t
       WHERE t.status = 'SOLD'
       """)
    Long countSoldTicketsByMonth();

    /**
     * Выручка
     * */
    @Query("""
       SELECT COALESCE(SUM(t.cost), 0)
       FROM Ticket t
       WHERE t.status = 'SOLD'
       """)
    BigDecimal getRevenueByMonth();
}
