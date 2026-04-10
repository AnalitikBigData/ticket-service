package com.example.ticketService.services;

import com.example.ticketService.RabbitMQService.RabbitMQHandler;
import com.example.ticketService.configs.RabbitMQConfig;
import com.example.ticketService.contracts.*;
import com.example.ticketService.dto.CommonMessage;
import com.example.ticketService.dto.TicketDto;
import com.example.ticketService.dto.Scenario;
import com.example.ticketService.entities.*;
import com.example.ticketService.repositories.EventRepository;
import com.example.ticketService.repositories.PurchaseRepository;
import com.example.ticketService.repositories.TicketRepository;
import com.example.ticketService.repositories.UserRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Service
public class PurchaseService {
    private static final Logger log = LoggerFactory.getLogger(PurchaseService.class);
    private final RabbitTemplate rabbitTemplate;
    private final TicketRepository ticketRepository;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseInternalService purchaseInternalService;

    public PurchaseService(TicketRepository ticketRepository,
                           PurchaseRepository purchaseRepository,
                           RabbitTemplate rabbitTemplate,
                           PurchaseInternalService purchaseInternalService) {
        this.ticketRepository = ticketRepository;
        this.purchaseRepository = purchaseRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.purchaseInternalService = purchaseInternalService;
    }

    /**
     * Покупка билетов на меропритие
     * */
    //@CircuitBreaker(name = "buyTickets", fallbackMethod = "buyTicketsFallback")
    //@Bulkhead(name = "buyTickets", type = Bulkhead.Type.THREADPOOL)
    @TimeLimiter(name = "buyTickets")
    @Retry(name = "buyTickets")
    public CompletableFuture<Response> buyTickets(BuyTicketsByEventIdRequest request)
    {
        return CompletableFuture.completedFuture(purchaseInternalService.buyTickets(request));
    }


    /**
     * Получение покупок по Id пользователя
     * */
    public List<GetPurchasesByUserIdResponse> getPurchasesByUserId(Long userId) {

        List<Purchase> purchases =
                purchaseRepository.findByUserUserId(userId);

        return purchases.stream()
                .map(this::mapGetPurchasesByUserIdResponse)
                .toList();
    }

    /**
     * Отмена покупки пользовтаелем
     * */
    @Transactional
    public List<GetPurchasesByUserIdResponse> cancelPurchaseById(CancelPurchaseByIdRequest request) {
        Purchase purchase = purchaseRepository.findById(request.getPurchaseId())
                .orElseThrow(() -> new RuntimeException("Purchase not found"));

        if (!purchase.getStatus().equals(Constants.PurchaseStatus.PAID.toString())) {
            throw new RuntimeException("Purchase cannot be cancelled");
        }

        int cancelCount = request.getCount();
        List<Ticket> tickets = ticketRepository.findByPurchasePurchaseId(purchase.getPurchaseId());

        if (cancelCount > tickets.size()) {
            throw new RuntimeException("Cancel count exceeds purchased tickets");
        }

        User user = purchase.getUser();

        Event event = purchase.getTickets().stream()
                .findFirst()
                .map(Ticket::getEvent)
                .orElse(null);

        List<Ticket> ticketsToCancel = tickets.subList(0, cancelCount);
        for (Ticket ticket : ticketsToCancel) {
            ticket.setStatus(Constants.TicketStatus.AVAILABLE.toString());
            ticket.setPurchase(null);
        }
        ticketRepository.saveAll(ticketsToCancel);

        if (tickets.size() == cancelCount) {
            purchase.setStatus(Constants.PurchaseStatus.CANCELLED.toString());
            purchaseRepository.save(purchase);
        }

        String message = String.format("%s|%s",
                user.getNameUser(),
                event != null ? event.getName() : ""
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.TICKET_CANCELLED_TOPIC,
                message
        );

        return purchaseRepository.findByUserUserId(purchase.getUser().getUserId())
                .stream()
                .map(this::mapGetPurchasesByUserIdResponse)
                .toList();
    }


    // fallback методы

    /**
     * fallback для покупки билетов
     * */
    public CompletableFuture<Response> buyTicketsFallback(Throwable ex) {

        log.error("Fallback: причина {}: сообщение {}", ex.getClass().getName(), ex.getMessage());

        var errResponse = new ErrorResponse();
        errResponse.setMessage("Оплата не прошла, попробуйте позже.");

        return CompletableFuture.completedFuture(errResponse);
    }

    // маппинги
    private BuyTicketsByEventIdResponse mapBuyTicketsByEventIdResponse(Purchase purchase, Event event, List<Ticket> tickets, String message){
        var map = mapTicketsDto(tickets);

        var response = new BuyTicketsByEventIdResponse();
        response.setIdPurchase(purchase.getPurchaseId());
        response.setDatePurchase(purchase.getDate());
        response.setStatePurchase(purchase.getStatus());
        response.setNameEvent(event.getName());
        response.setCityEvent(event.getCity());
        response.setDateEvent(event.getDate());
        response.setTickets(map);
        response.setMessage(message);

        return response;
    }

    private List<TicketDto> mapTicketsDto(List<Ticket> tickets){
        return tickets.stream()
                .map(t -> new TicketDto(
                        t.getTicketId(),
                        t.getNumber(),
                        t.getCost(),
                        t.getStatus()
                ))
                .toList();
    }

    private GetPurchasesByUserIdResponse mapGetPurchasesByUserIdResponse(Purchase purchase) {

        BigDecimal totalCost = purchase.getTickets() == null || purchase.getTickets().isEmpty()
                ? BigDecimal.ZERO
                : purchase.getTickets().stream()
                .map(Ticket::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String eventName = null;
        LocalDateTime eventDate = null;

        if (purchase.getTickets() != null && !purchase.getTickets().isEmpty()) {
            Event event = purchase.getTickets().get(0).getEvent();
            eventName = event.getName();
            eventDate = event.getDate();
        }

        return new GetPurchasesByUserIdResponse(
                purchase.getDate(),
                eventName,
                eventDate,
                totalCost,
                purchase.getStatus()
        );
    }
}


@Service
class PurchaseInternalService{
    private static final Logger log = LoggerFactory.getLogger(PurchaseInternalService.class);
    private final TicketRepository ticketRepository;
    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RabbitMQHandler rabbitMQHandler;

    // тест для попыток retry
    private final AtomicInteger attemptCounter = new AtomicInteger(0);
    private final AtomicInteger lastPattern = new AtomicInteger(-2);
    //boolean[] flags = {true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false};
    //int[] delays = {5000, 10, 10, 10, 10, 4000, 4000, 4000, 10, 10, 10, 10, 10, 10, 10};

    private final Map<Integer, Scenario> testScenarios = new ConcurrentHashMap<>() {{
        put(0, new Scenario(6000, List.of(true, true, true), "TIMEOUT"));
        put(1, new Scenario(0, List.of(true, true, false), "RETRY"));
        put(2, new Scenario(0, List.of(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true), "CIRCUIT BREAKER"));
        put(3, new Scenario(2000, List.of(false), "BULKHEAD"));
        put(4, new Scenario(0, List.of(true, true, true), "FALLBACK"));
        put(5, new Scenario(0, List.of(true, true, true, true, false, false, false, false), "FALLBACK_MSG"));
        //put(-1, new Scenario(0, List.of(false), "WORK"));
    }};

    public PurchaseInternalService(TicketRepository ticketRepository,
                            PurchaseRepository purchaseRepository,
                            UserRepository userRepository,
                            EventRepository eventRepository,
                            RabbitMQHandler rabbitMQHandler)
    {
        this.ticketRepository = ticketRepository;
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.rabbitMQHandler = rabbitMQHandler;
    }

    @Transactional
    @Bulkhead(name = "buyTickets", type = Bulkhead.Type.SEMAPHORE)
    Response buyTickets(BuyTicketsByEventIdRequest request){
        // нужно для теста паттерна потом убрать
        /*var check = (request.getTestPattern() != null) ? request.getTestPattern() : -1;
        Scenario scenario = testScenarios.getOrDefault(check, new Scenario(0, List.of(false), "WORK"));

        if (lastPattern.getAndSet(check) != check) {
            log.info("Сценарий изменился на {}, сбрасываем счетчик попыток.", check);
            attemptCounter.set(0);
        }*/
        // для сообщения
        String msg = null;

        // задрежка для Bulkhead

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        var attempt = attemptCounter.getAndIncrement();
        //var flags = scenario.flags();
        //var isFail = (attempt < flags.size()) ? flags.get(attempt) : false;

        var inf = "Попытка " + attempt;
        log.info(inf);

        /*if (isFail)
        {
            log.info("Попытка {}: Имитация ошибки для {}", attempt, scenario.pattern());
            try {
                if (scenario.delay() > 0) Thread.sleep(scenario.delay());
                //throw new RuntimeException("Error");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //return CompletableFuture.failedFuture(new RuntimeException("Error"));
            //throw new RuntimeException("Error");
        }*/

        // проверки
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.getStatus().equals(Constants.EventStatus.ACTIVE.toString())) {
            throw new RuntimeException("Event is not active");
        }

        Pageable limit = PageRequest.of(0, request.getCount());

        // ищем билеты для покупки
        List<Ticket> tickets = ticketRepository.findAndLockAvailableTickets(
                event.getEventId(),
                Constants.TicketStatus.AVAILABLE.toString(),
                limit);

        if (tickets.size() < request.getCount()) throw new RuntimeException("Not enough tickets available");

        var purchase = new Purchase();
        purchase.setUser(user);
        purchase.setDate(LocalDateTime.now());
        purchase.setStatus(Constants.PurchaseStatus.PAID.toString());

        // сохраняем покупку пользвоателя
        purchaseRepository.save(purchase);

        // устанвливаем билетам в покупке статус - куплен
        for (Ticket ticket : tickets) {
            ticket.setPurchase(purchase);
            ticket.setStatus(Constants.TicketStatus.SOLD.toString());
        }

        // сохраняем изм билеты
        ticketRepository.saveAll(tickets);

        // отправка очереди
        try {
            // отправка сообщений
            Map<String, Object> data = Map.of(
                    "userId", user.getUserId(),
                    "eventId", event.getEventId(),
                    "userName", user.getNameUser(),
                    "eventName", event.getName(),
                    "ticketCount", tickets.size()
            );

            var message = new CommonMessage(
                    "TICKET_PURCHASED",
                    LocalDateTime.now(),
                    data);

            rabbitMQHandler.sendMessage(
                    RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.TICKET_PURCHASED_TOPIC,
                    message, false);
        }
        catch (Exception e) {
            msg = "Билет забронирован, уведомление будет отправлено позже";
            log.error("Ошибка публикации события ticket.purchased. Сообщение для публикации для ручной отправки", e);
        }

        return mapBuyTicketsByEventIdResponse(purchase, event, tickets, msg);
    }

    private BuyTicketsByEventIdResponse mapBuyTicketsByEventIdResponse(Purchase purchase, Event event, List<Ticket> tickets, String message){
        var map = mapTicketsDto(tickets);

        var response = new BuyTicketsByEventIdResponse();
        response.setIdPurchase(purchase.getPurchaseId());
        response.setDatePurchase(purchase.getDate());
        response.setStatePurchase(purchase.getStatus());
        response.setNameEvent(event.getName());
        response.setCityEvent(event.getCity());
        response.setDateEvent(event.getDate());
        response.setTickets(map);
        response.setMessage(message);

        return response;
    }

    private List<TicketDto> mapTicketsDto(List<Ticket> tickets){
        return tickets.stream()
                .map(t -> new TicketDto(
                        t.getTicketId(),
                        t.getNumber(),
                        t.getCost(),
                        t.getStatus()
                ))
                .toList();
    }
}