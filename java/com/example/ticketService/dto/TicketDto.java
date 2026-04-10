package com.example.ticketService.dto;

import java.math.BigDecimal;

/**
 * Модель билета
 * */
public class TicketDto {
    /**
     * ID билета
     * */
    private Long id;

    /**
     * Номер билета
     * */
    private Integer number;

    /**
     * Стоимость билета
     * */
    private BigDecimal cost;

    /**
     * Статус билета
     * */
    private String state;

    public TicketDto(){

    }
    public TicketDto(Long id, Integer number, BigDecimal cost, String state){
        this.id = id;
        this.number = number;
        this.cost = cost;
        this.state = state;
    }


    /**
     * Геттер ID билета
     * */
    public Long getId() {
        return id;
    }

    /**
     * Сеттер ID билета
     * */
    public void setId(Long id) {
        this.id = id;
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
    public String getState(){
        return this.state;
    }

    /**
     * Сеттер состояния билета
     * */
    public void setState(String state){
        this.state = state;
    }
}
