package com.example.ticketService.contracts;

import java.time.LocalDateTime;

/**
 * Ответ на получение всех событий
 */
public class GetEventsResponse {

    /**
     * ID меропрития
     * */
    private final Long eventId;

    /**
     * Название мероприятия
     * */
    private final String name;

    /**
     * Город мероприятия
     * */
    private final String city;

    /**
     * Категория мероприятия
     * */
    private final String category;

    /**
     * Дата мероприятия
     * */
    private final LocalDateTime date;

    public GetEventsResponse(Long eventId,
                             String name,
                             String city,
                             String category,
                             LocalDateTime date) {
        this.eventId = eventId;
        this.name = name;
        this.city = city;
        this.category = category;
        this.date = date;
    }

    /**
     * Геттер ID мероприятия
     * */
    public Long getEventId() {
        return eventId;
    }

    /**
     * Геттер название мероприятия
     * */
    public String getName() {
        return name;
    }

    /**
     * Геттер города мероприятия
     * */
    public String getCity() {
        return city;
    }

    /**
     * Геттер категории мероприятия
     * */
    public String getCategory() {
        return category;
    }

    /**
     * Геттер даты проведения мероприятия
     * */
    public LocalDateTime getDate() {
        return date;
    }
}
