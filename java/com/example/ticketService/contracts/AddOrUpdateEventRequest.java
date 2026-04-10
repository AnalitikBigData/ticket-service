package com.example.ticketService.contracts;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Запрос на изменение события
 */
public class AddOrUpdateEventRequest {

    /**
     * ID мероприятия
     */
    private Long eventId;

    /**
     * Название меропрития
     */
    @NotBlank(message = "Название обязатльно для заполнения")
    @Size(min = 3, max = 100, message = "Название мероприятия должно быть 3-100")
    private String name;

    /**
     * Город мероприятия
     */
    @NotBlank
    private String city;

    /**
     * Категория мероприятия
     */
    @NotBlank
    private String category;

    /**
     * Дата проведения мероприятия
     */
    @Future
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    /**
     * Количество мест
     */
    @NotNull(message = "Кол-во мест меропрития не должно быть пустым")
    @Min(value = 1, message = "Кол-во мест должно быть не меньше 1")
    private Integer numberSeats;

    /**
     * Статус мероприятия
     */
    @NotBlank
    private String status;

    /**
     * Геттер ID события
     */
    public Long getEventId() {
        return eventId;
    }

    /**
     * Сеттер ID мероприятия
     */
    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    /**
     * Геттер названия
     */
    public String getName() {
        return name;
    }

    /**
     * Сеттер названия
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Геттер города
     */
    public String getCity() {
        return city;
    }

    /**
     * Сеттер города
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Геттер категории
     */
    public String getCategory() {
        return category;
    }

    /**
     * Сеттер категории
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Геттер даты проведения мероприятия
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Сеттер даты проведения мероприятия
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Геттер кол-ва мест
     */
    public Integer getNumberSeats() {
        return numberSeats;
    }

    /**
     * Сеттер кол-ва мест
     */
    public void setNumberSeats(Integer numberSeats) {
        this.numberSeats = numberSeats;
    }

    /**
     * Геттер статуса мероприятия
     */
    public String getStatus() {
        return status;
    }

    /**
     * Сеттер статуса мероприятия
     */
    public void setStatus(String status) {
        this.status = status;
    }
}

