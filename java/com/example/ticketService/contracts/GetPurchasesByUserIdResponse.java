package com.example.ticketService.contracts;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Ответ на получение всех покупок пользователя
 */
public class GetPurchasesByUserIdResponse {
    /**
     * Дата покупки билета на меропрития
     */
    private LocalDateTime datePurchase;

    /**
     * Название меропрития
     */
    private String nameEvent;

    /**
     * Дата проведение мероприятия
     */
    private LocalDateTime dateEvent;

    /**
     * Стоимсть покупки
     */
    private BigDecimal cost;

    /**
     * Статус покупки
     */
    private String state;

    public GetPurchasesByUserIdResponse(LocalDateTime datePurchase,
            String nameEvent,
            LocalDateTime dateEvent,
            BigDecimal cost, String state)
    {
        this.datePurchase = datePurchase;
        this.nameEvent = nameEvent;
        this.dateEvent = dateEvent;
        this.cost = cost;
        this.state = state;
    }

    /**
     * Геттер даты покупки
     */
    public LocalDateTime getDatePurchase() {
        return datePurchase;
    }

    /**
     * Сеттер даты покупки
     */
    public void setDatePurchase(LocalDateTime datePurchase) {
        this.datePurchase = datePurchase;
    }

    /**
     * Геттер названия мероприятия
     */
    public String getNameEvent() {
        return nameEvent;
    }

    /**
     * Сеттер названия мероприятия
     */
    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }

    /**
     * Геттер даты события
     */
    public LocalDateTime getDateEvent() {
        return dateEvent;
    }

    /**
     * Сеттер даты события
     */
    public void setDateEvent(LocalDateTime dateEvent) {
        this.dateEvent = dateEvent;
    }

    /**
     * Геттер стоимости
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * Сеттер стоимости
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * Геттер статуса покупки
     */
    public String getState() {
        return state;
    }

    /**
     * Сеттер статуса покупки
     */
    public void setState(String state) {
        this.state = state;
    }
}
