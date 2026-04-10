package com.example.ticketService.contracts;

import com.example.ticketService.dto.TicketDto;
import java.util.List;

/**
 * Ответ на запрос получения доступных билетов на мероприятия
 * */
public class GetAvailableTicketsByEventIdResponse {
    /**
     * ID меропрития
     * */
    private Long id;

    /**
     * Название мероприятия
     * */
    private String name;

    /**
     * Билеты мероприятия
     * */
    private List<TicketDto> tickets;

    /**
     * Конструктор
     * */
    public GetAvailableTicketsByEventIdResponse(Long id, String name, List<TicketDto> tickets){
        this.id = id;
        this.name = name;
        this.tickets = tickets;
    }

    /**
     * Геттер ID мероприятия
     * */
    public Long getId() {
        return id;
    }

    /**
     * Сеттер ID мероприятия
     * */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Название мероприятия
     * */
    public String getName() {
        return name;
    }

    /**
     * Сеттер названия мероприятия
     * */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Геттер билетов мероприятия
     * */
    public List<TicketDto> getTickets() {
        return tickets;
    }

    /**
     * Сеттер билетов мероприятия
     * */
    public void setTickets(List<TicketDto> tickets) {
        this.tickets = tickets;
    }
}
