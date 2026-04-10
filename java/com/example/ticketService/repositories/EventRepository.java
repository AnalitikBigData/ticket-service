package com.example.ticketService.repositories;

import com.example.ticketService.entities.Event;
import com.example.ticketService.entities.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//TODO проверить запросы на * и зменить на поля
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT * FROM Events e WHERE " +
            "(:category IS NULL OR UPPER(e.Category) = UPPER(:category)) AND " +
            "(:dateStart IS NULL OR e.Date >= :dateStart) AND " +
            "(:dateEnd IS NULL OR e.Date <= :dateEnd)",
            countQuery = "SELECT count(*) FROM Events e WHERE " +
                    "(:category IS NULL OR UPPER(e.Category) = UPPER(:category)) AND " +
                    "(:dateStart IS NULL OR e.Date >= :dateStart) AND " +
                    "(:dateEnd IS NULL OR e.Date <= :dateEnd)",
            nativeQuery = true)
    Page<Event> findByFilters(
            @Param("category") String category,
            @Param("dateStart") LocalDateTime dateStart,
            @Param("dateEnd") LocalDateTime dateEnd,
            Pageable pageable
    );

    @Query("SELECT e.id FROM Event e ORDER BY e.id desc LIMIT 1")
    Long getCurrentId();

    /**
     * Добавление / изменение меропрития
     * */
    @Modifying
    @Transactional
    @Query(value = "MERGE INTO EVENTS (EVENTID, NAME, CITY, CATEGORY, DATE, NUMBERSEATS, STATUS, UPDATEDDATE) " +
            "KEY (EVENTID) " +
            "VALUES (:#{#event.eventId}, :#{#event.name}, :#{#event.city}, :#{#event.category}, " +
            ":#{#event.date}, :#{#event.numberSeats}, :#{#event.status}, CURRENT_TIMESTAMP)",
            nativeQuery = true)
    void upsertEvent(@Param("event") Event event);

    @Query(value = "SELECT DISTINCT e.* FROM events e " +
            "JOIN tickets t ON e.eventid = t.eventid " +
            "WHERE e.status = :eventStatus " +
            "AND t.status = :ticketStatus " +
            "AND e.date >= :date " +
            "ORDER BY e.date ASC " +
            "LIMIT 1",
            nativeQuery = true)
    Optional<Event> getNearestEvent(
            @Param("eventStatus") String eventStatus,
            @Param("ticketStatus") String ticketStatus,
            @Param("date") LocalDateTime date
    );
}
