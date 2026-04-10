package com.example.ticketService.controllers;

import com.example.ticketService.contracts.*;
import com.example.ticketService.services.EventService;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/events")
@Tag(name = "Event Controller")
public class EventController {

    private final EventService eventService;
    private final RateLimiterRegistry rateLimiterRegistry;

    public EventController(EventService eventService, RateLimiterRegistry rateLimiterRegistry) {
        this.eventService = eventService;
        this.rateLimiterRegistry = rateLimiterRegistry;
    }

    @GetMapping("/getEvents")
    @Operation(summary = "Получение всех событий")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = GetEventsResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "429")
    })
    public ResponseEntity<List<GetEventsResponse>> getEvents(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateEnd,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer count,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest httpRequest)
    {
        // проверка на ip
        RateLimiter ipRateLimiter = rateLimiterRegistry.rateLimiter(httpRequest.getRemoteAddr());

        if (!ipRateLimiter.acquirePermission()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        return ResponseEntity.ok(eventService.getEvents(category, dateStart, dateEnd, page, count, sortBy, direction));
    }

    @GetMapping("/getEventById/{id}")
    @Operation(summary = "Получение события по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = GetEventByIdResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema())
            })
    })
    public ResponseEntity<GetEventByIdResponse> getEventById(@PathVariable Long id) {
        GetEventByIdResponse response = eventService.getEventById(id);
        if (response != null)
        {
            return ResponseEntity.ok(response);
        } else
        {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getNearestEvent")
    @Operation(summary = "Получение ближайшего события с дотсупными билетами")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = GetEventByIdResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema())
            })
    })
    public ResponseEntity<GetEventByIdResponse> getNearestEvent() {
        GetEventByIdResponse response = eventService.getNearestEvent();
        if (response != null)
        {
            return ResponseEntity.ok(response);
        } else
        {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/getTicketsByEventId")
    @Operation(summary = "Получение доступных билетов на меропритие")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = GetAvailableTicketsByEventIdResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema())
            })
    })
    public ResponseEntity<GetAvailableTicketsByEventIdResponse> getTicketsByEventId(@PathVariable Long id) {

        var response = eventService.getAvailableTicketsByEventId(id);

        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addOrUpdateEvent")
    @Operation(summary = "Добавление / обновление информации о меропритии")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Long.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "400", content = {
                    @Content(schema = @Schema())
            })
    })
    public ResponseEntity<?> addOrUpdateEvent(@Valid AddOrUpdateEventRequest request) {

        var response = eventService.addOrUpdateEvent(request);

        if (response == null) {
            return ResponseEntity.badRequest().body("Ошибка при добавлении и изменении меропрития");
        }
        return ResponseEntity.ok(response);
    }
}
