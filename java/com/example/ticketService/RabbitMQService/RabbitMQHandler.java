package com.example.ticketService.RabbitMQService;

import com.example.ticketService.dto.CommonMessage;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Сервис - обработчик брокера
 * */
@Component
public class RabbitMQHandler {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQHandler.class);
    private final RabbitTemplate rabbitTemplate;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public RabbitMQHandler(RabbitTemplate rabbitTemplate,
                           CircuitBreakerRegistry circuitBreakerRegistry){
        this.rabbitTemplate = rabbitTemplate;
        this.circuitBreakerRegistry = circuitBreakerRegistry;

        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slowCallRateThreshold(2)
                .slidingWindowSize(2)
                .minimumNumberOfCalls(2)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .waitDurationInOpenState(Duration.ofSeconds(2))
                .permittedNumberOfCallsInHalfOpenState(1)
                .recordExceptions(Throwable.class)
                .build();

        circuitBreakerRegistry.circuitBreaker("rabbitSendMessage", config);
    }

    //@CircuitBreaker(name = "rabbitSendMessage")
    public void sendMessage(String exchange, String routingKey, CommonMessage msg, boolean simulateError) {
        var cb = circuitBreakerRegistry.circuitBreaker("rabbitSendMessage");

        cb.executeRunnable(() ->
        {
            var metrics = cb.getMetrics();
            log.info("Circuit Breaker STATUS: {} | Fails: {} | Buffered: {} | Window {}",
                    cb.getState(),
                    metrics.getNumberOfFailedCalls(),
                    metrics.getNumberOfBufferedCalls(),
                    cb.getCircuitBreakerConfig().getSlidingWindowSize());

            if (simulateError) {
                throw new RuntimeException("RabbitMQ Error");
            }

            rabbitTemplate.convertAndSend(exchange, routingKey, msg);
            log.info("Событие {} отправлено в {}", msg.type(), routingKey);});
    }

    //fallback
}
