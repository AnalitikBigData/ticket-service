package com.example.ticketService.contracts;

import java.time.LocalDateTime;

/**
 * Ответ на получение события по ID
 * */
public class GetEventByIdResponse
{
    /**
     * ID мероприятия
     * */
    private final Long eventId;

    /**
     * Название меропрития
     * */
    private final String name;

    /**
     * Город меропрития
     * */
    private final String city;

    /**
     * Категория меропрития
     * */
    private final String category;

    /**
     * Дата проведения меропрития
     * */
    private final LocalDateTime date;

    /**
     * Кол-во мест на мероприятие
     * */
    private final Integer numberSeats;

    /**
     * Дата обновления
     * */
    private final LocalDateTime updatedDate;

    /**
     * Статус меропрития
     * */
    private final String status;

    public GetEventByIdResponse(Long eventId,
                                String name,
                                String city,
                                String category,
                                LocalDateTime date,
                                Integer numberSeats,
                                LocalDateTime updatedDate,
                                String status){
        this.eventId = eventId;
        this.name = name;
        this.city = city;
        this.category = category;
        this.date = date;
        this.numberSeats = numberSeats;
        this.updatedDate = updatedDate;
        this.status = status;
    }


    /**
     * Геттер ID меропрития
     * */
    public Long getEventId() {
        return eventId;
    }

    /**
     * Геттер название меропрития
     * */
    public String getName() {
        return name;
    }

    /**
     * Геттер города меропрития
     * */
    public String getCity() {
        return city;
    }

    /**
     * Геттер категории меропрития
     * */
    public String getCategory() {
        return category;
    }

    /**
     * Геттер даты проведения меропрития
     * */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Геттер кол-ва мест на меропритие
     * */
    public Integer getNumberSeats(){
        return numberSeats;
    }

    /**
     * Геттер даты обновления меропрития
     * */
    public LocalDateTime getUpdatedDate(){
        return updatedDate;
    }

    /**
     * Геттер статуса меропрития
     * */
    public String getStatus(){
        return status;
    }
}
