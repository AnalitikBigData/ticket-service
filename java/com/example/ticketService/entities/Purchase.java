package com.example.ticketService.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Покупка
 * */
@Entity
@Table(name = "Purchases", indexes = {
        @Index(name = "IDX_Purchases_User", columnList = "userId")
})
public class Purchase {

    /**
     * ID покупки
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PURCHASEID")
    private Long purchaseId;

    /**
     * Пользователь покупки
     * */
    @ManyToOne
    @JoinColumn(name = "USERID", nullable = false)
    private User user;

    /**
     * Дата покупки билетов
     * */
    @Column(name = "DATE", nullable = false)
    private LocalDateTime date;

    /**
     * Состояние билета
     * */
    @Column(nullable = false, length = 20)
    private String status;

    /**
     * Билеты в покупке
     * */
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    /**
     * Геттер ID покупки
     * */
    public Long getPurchaseId() {
        return purchaseId;
    }

    /**
     * Сеттер ID покупки
     * */
    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    /**
     * Геттер пользователя
     * */
    public User getUser() {
        return user;
    }

    /**
     * Сеттер пользователя
     * */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Геттер даты покупки
     * */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Сеттер даты покупки
     * */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Геттер состояния билета
     * */
    public String getStatus() {
        return status;
    }

    /**
     * Сеттер состояния билетов
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
