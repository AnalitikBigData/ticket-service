package com.example.ticketService.entities;

/**
 * Константы
 * */
public final class Constants {

    private Constants(){
    }

    /**
     * Категории меропритий
     * */
    public enum Category {
        CONCERT,
        THEATRE,
        SPORT
    }

    /**
     * Состояние мероприятий
     * */
    public enum EventStatus {
        ACTIVE,
        SCHEDULED,
        CANCELLED,
        POSTPONED,
        COMPLETED
    }

    /**
     * Состоние билетов
     * */
    public enum TicketStatus {
        AVAILABLE,
        RESERVED,
        SOLD,
        CANCELLED
    }

    /**
     * Состояние покупки
     * */
    public enum PurchaseStatus {
        CREATED,
        PAID,
        CANCELLED,
        REFUNDED
    }

    /**
     * Роли пользователей
     * */
    public enum RoleUser {
        ADMIN,
        USER,
        ORGANIZATION,
    }
}
