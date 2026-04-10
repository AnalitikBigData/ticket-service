package com.example.ticketService.repositories;

import com.example.ticketService.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//TODO проверить запросы на *
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Получение пользователя по логину
     * */
    Optional<User> findByLoginUser(String loginUser);

    /**
     * Получение пользоввтаеля по ID
     * */
    Optional<User> findByUserId(Long id);

    /**
     * Получение пользователей по роле
     * */
    List<User> findByRoleUser(String roleUser);

    /**
     * Кол-во зарегитсрированных пользователей
     * */
    @Query("""
       SELECT COUNT(u)
       FROM User u
       """)
    Long countUsers();
}
