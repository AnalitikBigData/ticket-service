package com.example.ticketService.contracts;

import com.example.ticketService.dto.TicketDto;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Ответ на покупку билетов мероприятия 
 * */
public class BuyTicketsByEventIdResponse extends Response {
    /**
     * ID покупки
     * */
    private Long idPurchase;

    /**
     * Дата покупки
     * */
    private LocalDateTime datePurchase;

    /**
     * Статус покупки
     * */
    private String statePurchase;

    /**
     * Название мероприятия
     * */
    private String nameEvent;

    /**
     * Город меропрития
     * */
    private String cityEvent;

    /**
     * Дата мероприятия
     * */
    private LocalDateTime dateEvent;

    /**
     * Билеты
     * */
    private List<TicketDto> tickets;

    /**
     * Дополнительное сообщение пользвоателю (мб пустое)
     * */
    private String message;

    /**
     * Геттер ID покупки
     * */
    public Long getIdPurchase() {
        return idPurchase;
    }

    /**
     * Сеттер ID покупки
     * */
    public void setIdPurchase(Long idPurchase) {
        this.idPurchase = idPurchase;
    }

    /**
     * Геттер даты покупки
     * */
    public LocalDateTime getDatePurchase() {
        return datePurchase;
    }

    /**
     * Сеттер даты покупки
     * */
    public void setDatePurchase(LocalDateTime datePurchase) {
        this.datePurchase = datePurchase;
    }

    /**
     * Геттер статуса покупки
     * */
    public String getStatePurchase() {
        return statePurchase;
    }

    /**
     * Сеттер статуса покупки
     * */
    public void setStatePurchase(String statePurchase) {
        this.statePurchase = statePurchase;
    }

    /**
     * Геттер названия мероприятия
     * */
    public String getNameEvent() {
        return nameEvent;
    }

    /**
     * Сеттер названия события
     * */
    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }

    /**
     * Геттер города меропрития
     * */
    public String getCityEvent() {
        return cityEvent;
    }

    /**
     * Сеттер города меропрития
     * */
    public void setCityEvent(String cityEvent) {
        this.cityEvent = cityEvent;
    }

    /**
     * Геттер даты события
     * */
    public LocalDateTime getDateEvent() {
        return dateEvent;
    }

    /**
     * Сеттер даты проведения меропрития
     * */
    public void setDateEvent(LocalDateTime dateEvent) {
        this.dateEvent = dateEvent;
    }

    /**
     * Геттер билетов
     * */
    public List<TicketDto> getTickets() {
        return tickets;
    }

    /**
     * Сеттер билетов
     * */
    public void setTickets(List<TicketDto> tickets) {
        this.tickets = tickets;
    }

    /**
     * Геттер сообщения пользователю
     * */
    public String getMessage(){
        return message;
    }

    /**
     * Сеттер сообщения пользователю
     * */
    public void setMessage(String msg){
        this.message = msg;
    }
}
