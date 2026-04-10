package com.example.ticketService.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Билет
 * */
@Entity
@Table(name = "Tickets",
        uniqueConstraints = @UniqueConstraint(columnNames = {"eventId", "number"}),
        indexes = @Index(name = "IDX_Tickets_Event", columnList = "eventId"))
public class Ticket {
    /**
     * ID билета
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TICKETID")
    private Long ticketId;

    /**
     * ID мероприятия
     * */
    @ManyToOne
    @JoinColumn(name = "EVENTID", nullable = false)
    private Event event;

    /**
     * ID покупки
     * */
    @ManyToOne
    @JoinColumn(name = "PURCHESID")
    private Purchase purchase;

    /**
     * Номер билета
     * */
    @Column(name = "NUMBER", nullable = false)
    private Integer number;

    /**
     * Стоимость билета
     * */
    @Column(name = "COST", nullable = false, precision = 10, scale = 2)
    private BigDecimal cost;

    /**
     * Состояние билета
     * */
    @Column(name = "STATUS", nullable = false, length = 20)
    private String status;

    /**
     * Геттер ID билета
     * */
    public Long getTicketId() {
        return ticketId;
    }

    /**
     * Сеттер ID билета
     * */
    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    /**
     * Геттер мероприятия
     * */
    public Event getEvent() {
        return event;
    }

    /**
     * Сеттер мероприятия
     * */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Геттер покупки
     * */
    public Purchase getPurchase() {
        return purchase;
    }

    /**
     * Сеттер покупки
     * */
    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    /**
     * Геттер номера билета
     * */
    public Integer getNumber() {
        return number;
    }

    /**
     * Сеттер номера билета
     * */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * Геттер стоимости билета
     * */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * Сеттер стоимости билета
     * */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * Геттер состояния билета
     * */
    public String getStatus() {
        return status;
    }

    /**
     * Сеттер состояния билета
     * */
    public void setStatus(String status) {
        this.status = status;
    }
}
