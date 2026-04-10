package com.example.ticketService.RabbitMQService;

import com.example.ticketService.configs.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Подписчик на отмену покупки
 * */
public class RabbitMQCancelConsumer {
    private static final Logger log = LoggerFactory.getLogger(RabbitMQCancelConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.CANCEL_QUEUE)
    public void handleCancel(String message, Channel channel, Message rawMessage) throws IOException {
        try {
            String[] parts = message.split("\\|");
            String userName = parts[0];
            String eventName = parts[1];

            String emailText = String.format(
                    "Уважаемый %s, вы отменили покупку на %s.",
                    userName,
                    eventName
            );

            log.info(emailText);
            channel.basicAck(rawMessage.getMessageProperties().getDeliveryTag(),
                    false);

        } catch (Exception e) {
            log.error("Ошибка обработки отмены", e);
            channel.basicNack(
                    rawMessage.getMessageProperties().getDeliveryTag(),
                    false,
                    false
            );
        }
    }

}
