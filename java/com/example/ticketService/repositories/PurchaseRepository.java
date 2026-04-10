package com.example.ticketService.repositories;

import com.example.ticketService.entities.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//TODO проверить запросы на *
@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    /**
     * Получение покупок по ID пользователя
     * */
    List<Purchase> findByUserUserId(Long userId);
}
