package com.example.ticketService.RabbitMQService;

import com.example.ticketService.configs.RabbitMQConfig;
import com.example.ticketService.dto.CommonMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Подписчик на покупку
 * */
@Component
public class RabbitMQPurchaseConsumer {
    private static final Logger log = LoggerFactory.getLogger(RabbitMQPurchaseConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void handleEmail(CommonMessage message, Channel channel, Message rawMessage) throws IOException {
        try {
            var data = message.data();

            String userName = String.valueOf(data.get("userName"));
            String eventName = String.valueOf(data.get("eventName"));
            Number countTickets = (Number) data.get("ticketCount");

            log.info("Уважаемый {}, вы купили {} билетов на {}", userName, countTickets, eventName);
            channel.basicAck(rawMessage.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            log.error("Ошибка обработки email-события", e);
            channel.basicNack(rawMessage.getMessageProperties().getDeliveryTag(), false, false);
        }
    }
}
