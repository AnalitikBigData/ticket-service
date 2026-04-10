package com.example.ticketService.contracts;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Запрос на покупку билетов на мероприятие
 * */
public class BuyTicketsByEventIdRequest {
    /**
     * ID события
     * */
    @NotNull(message = "Event ID обязателен")
    private final Long eventId;

    /**
     * ID пользователя
     * */
    @NotNull(message = "User ID обязателен")
    private final Long userId;

    /**
     * Количество билетов для покупки
     * */
    @NotNull(message = "Количество билетов обязательно")
    @Positive(message = "Кол-во билетов должно быть больше 0")
    private final Integer countTickets;

    /**
     * Временно для тестирвоания паттеран в номером словаре
     * */
    private Integer testPattern;


    public BuyTicketsByEventIdRequest(Long eventId, Long userId, Integer count){
        this.eventId = eventId;
        this.userId = userId;
        this.countTickets = count;
    }

    /**
     * Геттер ID события
     * */
    public Long getEventId() {
        return eventId;
    }

    /**
     * Геттер ID пользователя
     * */
    public Long getUserId() {
        return userId;
    }

    /**
     * Геттер Количество билетов для покупки
     * */
    public Integer getCount() {
        return countTickets;
    }

    /**
     * Проверка для паттерана (временно на проверку)
     * */
    public Integer getTestPattern() {
        return testPattern;
    }

    public void setTestPattern(Integer testPattern) {
        this.testPattern = testPattern;
    }
}

