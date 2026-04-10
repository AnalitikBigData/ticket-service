package com.example.ticketService.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурации брокера
 * */
@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE = "ticket.exchange";

    public static final String TICKET_PURCHASED_TOPIC = "ticket.purchased";
    public static final String EMAIL_QUEUE = "ticket.email.queue";
    public static final String ANALYTICS_PURCHASE_QUEUE = "ticket.analytics-purchase.queue";

    public static final String TICKET_CANCELLED_TOPIC = "ticket.cancelled";
    public static final String CANCEL_QUEUE = "ticket.cancel.queue";
    public static final String ANALYTICS_CANCEL_QUEUE = "ticket.analytic-cancel.queue";

    public static final String EVENT_UPDATED_TOPIC = "event.updated";
    public static final String EVENT_UPDATE_QUEUE = "event.update.queue";
    public static final String ANALYTICS_UPDATE_QUEUE = "ticket.analytic-update.queue";

    public static final String USER_REGISTERED_TOPIC = "user.registered";
    public static final String ADMIN_NOTIFY_QUEUE = "user.admin.notify.queue";
    public static final String ANALYTICS_REG_QUEUE = "ticket.analytics-reg.queue";

    public static final String ANALYTICS_QUEUE = "ticket.analytics.queue";

    public static final String ANALYTICS_QUEUE_PURCHASE = "ticket.analytics-purchase.queue";

    public static final String DLQ_QUEUE = "ticket.dlq";

    @Bean
    public TopicExchange ticketExchange() {
        return new TopicExchange(EXCHANGE);
    }


    @Bean
    public Queue dlqQueue() {
        return QueueBuilder.durable(DLQ_QUEUE).build();
    }

    // очередь email покупки
    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(EMAIL_QUEUE)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", DLQ_QUEUE)
                .build();
    }

    @Bean
    public Queue analyticPurchaseQueue(){
        return QueueBuilder
                .durable(ANALYTICS_PURCHASE_QUEUE)
                .build();
    }

    @Bean
    public Binding bindEmailQueue(Queue emailQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(emailQueue)
                .to(exchange)
                .with(TICKET_PURCHASED_TOPIC);
    }

    @Bean
    public Binding bindAnalyticsPurchaseQueue(Queue analyticPurchaseQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(analyticPurchaseQueue)
                .to(exchange)
                .with(TICKET_PURCHASED_TOPIC);
    }

    // Очередь для отмены
    @Bean
    public Queue cancelQueue() {

        return QueueBuilder.durable(CANCEL_QUEUE).build();
    }

    @Bean
    public Queue analyticCancelQueue() {

        return QueueBuilder
                .durable(ANALYTICS_CANCEL_QUEUE)
                .build();
    }

    @Bean
    public Binding bindCancelQueue(Queue cancelQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(cancelQueue)
                .to(exchange)
                .with(TICKET_CANCELLED_TOPIC);
    }

    @Bean
    public Binding bindAnalyticCancelQueue(Queue analyticCancelQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(analyticCancelQueue)
                .to(exchange)
                .with(TICKET_CANCELLED_TOPIC);
    }

    // очередь обновления мероприятия
    @Bean
    public Queue eventUpdateQueue() {
        return QueueBuilder.durable(EVENT_UPDATE_QUEUE).build();
    }

    @Bean
    public Queue analyticsEventUpdateQueue() {
        return QueueBuilder.durable(ANALYTICS_UPDATE_QUEUE).build();
    }

    @Bean
    public Binding bindEventUpdateQueue(Queue eventUpdateQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(eventUpdateQueue)
                .to(exchange)
                .with(EVENT_UPDATED_TOPIC);
    }

    @Bean
    public Binding bindAnalyticEventUpdateQueue(Queue analyticsEventUpdateQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(analyticsEventUpdateQueue)
                .to(exchange)
                .with(EVENT_UPDATED_TOPIC);
    }

    // очередь регистрации
    @Bean
    public Queue adminNotifyQueue() {

        return QueueBuilder.durable(ADMIN_NOTIFY_QUEUE).build();
    }

    @Bean
    public Queue analyticRegistration(){
        return QueueBuilder.durable(ANALYTICS_REG_QUEUE).build();
    }

    @Bean
    public Binding bindAdminNotifyQueue(Queue adminNotifyQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(adminNotifyQueue)
                .to(exchange)
                .with(USER_REGISTERED_TOPIC);
    }

    @Bean
    public Binding bindAnalyticRegistration(Queue analyticRegistration, TopicExchange exchange){
        return BindingBuilder
                .bind(analyticRegistration)
                .to(exchange)
                .with(USER_REGISTERED_TOPIC);
    }

    // очередь аналитики
    @Bean
    public Queue analyticsQueue() {
        return QueueBuilder
                .durable(ANALYTICS_QUEUE)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", DLQ_QUEUE)
                .build();
    }

    @Bean
    public Binding bindAnalyticsQueue(Queue analyticsQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(analyticsQueue)
                .to(exchange)
                .with(TICKET_PURCHASED_TOPIC);
    }

    // Очередь для покупки
    @Bean
    public Queue analyticsPurchaseQueue() {
        return QueueBuilder.durable(ANALYTICS_QUEUE_PURCHASE).build();
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL); // ручное подтверждение
        return factory;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         JacksonJsonMessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
}