package com.example.ticketService.services;

import com.example.ticketService.configs.RabbitMQConfig;
import com.example.ticketService.contracts.*;
import com.example.ticketService.dto.TicketDto;
import com.example.ticketService.entities.Event;
import com.example.ticketService.entities.Ticket;
import com.example.ticketService.repositories.EventRepository;
import com.example.ticketService.repositories.TicketRepository;
import com.example.ticketService.entities.Constants;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
//todo посм как использовтаь автоконтсруткор библиотеки ???
public class EventService {
    private final RabbitTemplate rabbitTemplate;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    public EventService(
            EventRepository eventRepository,
            TicketRepository ticketRepository,
            RabbitTemplate rabbitTemplate)
    {

        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Получение событий
     * */
    public List<GetEventsResponse> getEvents(String category, LocalDateTime dateStart, LocalDateTime dateEnd, Integer page, Integer count, String sortBy, String direction)
    {
        Map<String, String> sortMapping = Map.of(
                "date", "Date",
                "name", "Name"
        );

        String sortColumn = sortMapping.getOrDefault(sortBy.toLowerCase(), "Date");
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortColumn).descending()
                : Sort.by(sortColumn).ascending();

        // for filters
        String formattedCategory = null;
        if (category != null && !category.trim().isEmpty()) {
            var isValid = Arrays.stream(Constants.Category.values())
                    .anyMatch(c -> c.name().equalsIgnoreCase(category.trim()));

            if (!isValid) return Collections.emptyList();

            formattedCategory = category.trim().toUpperCase();
        }

        Pageable pageable = PageRequest.of(page, count, sort);

        return eventRepository.findByFilters(formattedCategory, dateStart, dateEnd, pageable)
                .stream()
                .map(this::mapToGetEventsResponse)
                .collect(Collectors.toList());
    }

    /**
     * Получение события по ID
     * */
    public GetEventByIdResponse getEventById(Long id)
    {
        return eventRepository.findById(id)
                .map(this::mapToGetEventByIdResponse)
                .orElse(null);
    }

    public GetEventByIdResponse getNearestEvent() {
        return eventRepository.getNearestEvent(
                        Constants.EventStatus.ACTIVE.toString(),
                        Constants.TicketStatus.AVAILABLE.toString(),
                        LocalDateTime.now())
                .map(this::mapToGetEventByIdResponse)
                .orElse(null); // Если ничего не нашли
    }


    /**
     * Получение доступных билетов на меропритие
     * */
    //TODO испр ответ - доступные билеты на меропритие шт мин-макс стоимость
    public GetAvailableTicketsByEventIdResponse getAvailableTicketsByEventId(Long eventId)
    {
        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        List<Ticket> tickets = ticketRepository
                .findByEventEventIdAndStatus(
                        eventId,
                        Constants.TicketStatus.AVAILABLE.toString()
                );

        List<TicketDto> ticketsMap = mapTickets(tickets);

        return mapGetAvailableTicketsByEventIdResponse(event, ticketsMap);
    }

    /**
     * Добавление / обновление информации о событие
     * */
    @Transactional
    //TODO логика добавления / обновления события исправить БД на guid или как это в java
    public Long addOrUpdateEvent(AddOrUpdateEventRequest request)
    {
        Event event = new Event();

        

        //TODO вынести в маппер
        event.setName(request.getName());
        event.setCity(request.getCity());
        event.setCategory(request.getCategory());
        event.setDate(request.getDate());
        event.setNumberSeats(request.getNumberSeats());
        event.setStatus(request.getStatus());
        event.setUpdatedDate(LocalDateTime.now());

        eventRepository.upsertEvent(event);

        //todo str -> json
        String message = String.format("%d|%s|%s",
                event.getEventId(),
                event.getName(),
                event.getDate()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.EVENT_UPDATED_TOPIC,
                message
        );

        return event.getEventId();
    }


    //todo найти библ с авто маппингом
    // маппинги
    private GetEventByIdResponse mapToGetEventByIdResponse(Event event){
        return new GetEventByIdResponse(
                event.getEventId(),
                event.getName(),
                event.getCity(),
                event.getCategory(),
                event.getDate(),
                event.getNumberSeats(),
                event.getUpdatedDate(),
                event.getStatus()
        );
    }

    private GetEventsResponse mapToGetEventsResponse(Event event) {
        return new GetEventsResponse(
                event.getEventId(),
                event.getName(),
                event.getCity(),
                event.getCategory(),
                event.getDate()
        );
    }

    private List<TicketDto> mapTickets(List<Ticket> tickets){
        return tickets.stream()
                .map(ticket -> {
                    TicketDto dto = new TicketDto();
                    dto.setId(ticket.getTicketId());
                    dto.setNumber(ticket.getNumber());
                    dto.setCost(ticket.getCost());
                    return dto;
                })
                .toList();
    }

    private GetAvailableTicketsByEventIdResponse mapGetAvailableTicketsByEventIdResponse(Event event,
                                                                                         List<TicketDto> tickets){
        return new GetAvailableTicketsByEventIdResponse(
                event.getEventId(),
                event.getName(),
                tickets);
    }
}