package com.example.ticketService.controllers;

import com.example.ticketService.contracts.*;
import com.example.ticketService.services.PurchaseService;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/purchases")
@Tag(name = "Purchase Controller")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final RateLimiterRegistry rateLimiterRegistry;

    public PurchaseController(PurchaseService purchaseService, RateLimiterRegistry rateLimiterRegistry) {

        this.purchaseService = purchaseService;
        this.rateLimiterRegistry = rateLimiterRegistry;
    }

    @PostMapping("/buyTickets")
    @Operation(summary = "Покупка билетов")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = BuyTicketsByEventIdResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "429"),
            @ApiResponse(responseCode = "400")
    })
    public CompletableFuture<ResponseEntity<Response>> buyTickets(@RequestBody BuyTicketsByEventIdRequest request, HttpServletRequest httpRequest) {
        // проверка на ip
        RateLimiter ipRateLimiter = rateLimiterRegistry.rateLimiter(httpRequest.getRemoteAddr());

        if (!ipRateLimiter.acquirePermission()) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build()
            );
        }

        return purchaseService.buyTickets(request)
                .thenApply(response -> {
                    if (response instanceof ErrorResponse) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
                    return ResponseEntity.ok(response);
                });
    }

    @GetMapping("/getPurchasesByUserId/{userId}")
    @Operation(summary = "Получение покупок пользователя по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = GetPurchasesByUserIdResponse.class), mediaType = "application/json")
            })
    })
    public ResponseEntity<List<GetPurchasesByUserIdResponse>> getPurchasesByUserId(@PathVariable Long userId) {
        List<GetPurchasesByUserIdResponse> response = purchaseService.getPurchasesByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancelPurchase")
    @Operation(summary = "Отмена покупки")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = GetPurchasesByUserIdResponse.class), mediaType = "application/json")
            })
    })
    public ResponseEntity<List<GetPurchasesByUserIdResponse>> cancelPurchase(@RequestBody CancelPurchaseByIdRequest request) {
        List<GetPurchasesByUserIdResponse> response = purchaseService.cancelPurchaseById(request);
        return ResponseEntity.ok(response);
    }
}
