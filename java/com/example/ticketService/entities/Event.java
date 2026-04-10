package com.example.ticketService.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Меропритие
 * */
@Entity
@Table(name = "EVENTS", indexes = {
        @Index(name = "IDX_Events_Date", columnList = "date"),
        @Index(name = "IDX_Events_Category", columnList = "category")
})
public class Event {

    /**
     * ID меропрития
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENTID")
    private Long eventId;

    /**
     * Название меропрития
     * */
    @Column(name = "NAME", nullable = false, length = 255)
    private String name;

    /**
     * Город проведения мероприятия
     * */
    @Column(name = "CITY", nullable = false, length = 100)
    private String city;

    /**
     * Категория мероприятия
     * */
    @Column(name = "CATEGORY", nullable = false, length = 50)
    private String category;

    /**
     * Дата мероприятия
     * */
    @Column(name = "DATE", nullable = false)
    private LocalDateTime date;

    /**
     * Кол-во мест на меропритие
     * */
    @Column(name = "NUMBERSEATS", nullable = false)
    private Integer numberSeats;

    /**
     * Дата обновления
     * */
    @Column(name = "UPDATEDDATE")
    private LocalDateTime updatedDate;

    /**
     * Состояние меропрития
     * */
    @Column(name = "STATUS", nullable = false, length = 20)
    private String status;

    /**
     * Билеты на меропритие
     * */
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    /**
     * Геттер ID мероприятия
     * */
    public Long getEventId() {
        return eventId;
    }

    /**
     * Геттер названия
     * */
    public String getName() {
        return name;
    }

    /**
     * Геттер города
     * */
    public String getCity() {
        return city;
    }

    /**
     * Геттер категории
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
     * Геттер кол-ва мест
     * */
    public Integer getNumberSeats(){
        return numberSeats;
    }

    /**
     * Геттер обновления меропрития
     * */
    public LocalDateTime getUpdatedDate(){
        return updatedDate;
    }

    /**
     * Геттер состояния мероприятия
     * */
    public String getStatus(){
        return status;
    }

    /**
     * Сеттер ID мероприятия
     * */
    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    /**
     * Сеттер названия
     * */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Геттер города проведения меропрития
     * */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Сеттер категории меропрития
     * */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Сеттер даты мероприятия
     * */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Сеттер кол-ва мест мероприятия
     * */
    public void setNumberSeats(Integer numberSeats) {
        this.numberSeats = numberSeats;
    }

    /**
     * Сеттер ID мероприятия
     * */
    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * Геттер ID мероприятия
     * */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Геттер билетов
     * */
    public List<Ticket> getTickets() {
        return tickets;
    }

    /**
     * Сеттер билетов
     * */
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
