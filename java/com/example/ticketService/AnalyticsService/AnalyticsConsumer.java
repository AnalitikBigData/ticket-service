package com.example.ticketService.AnalyticsService;

import com.example.ticketService.configs.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//todo update get msg from R MQ handler
/**
 * Подписчик на аналитику
 * */
@Component
public class AnalyticsConsumer {
    private static final Logger log = LoggerFactory.getLogger(AnalyticsConsumer.class);

    /**
     * Подписчик на анлитику
     * */
    @RabbitListener(queues = RabbitMQConfig.ANALYTICS_QUEUE)
    public void handleAnalytics(String message, Channel channel, Message rawMessage) throws IOException {

        try{
            String[] parts = message.split(";");
            int registered = 0;
            int sold = 0;
            double revenue = 0.0;

            for (String part : parts) {
                String[] info = part.split("=");
                if (info.length != 2) continue;

                switch (info[0].trim()) {
                    case "Registered":
                        registered = Integer.parseInt(info[1].trim());
                        break;
                    case "Sold":
                        sold = Integer.parseInt(info[1].trim());
                        break;
                    case "Revenue":
                        revenue = Double.parseDouble(info[1].trim());
                        break;
                }
            }

            writeToFile(message);
            log.info("[ANALYTICS] Registered={}, Sold={}, Revenue={}", registered, sold, revenue);
        }
        catch (Exception e) {
            log.error("[ANALYTICS] Ошибка обработки: ", e);
            channel.basicNack(rawMessage.getMessageProperties().getDeliveryTag(), false, false);
        }
    }

    /**
     * Подписчик на регистрацию
     * */
    @RabbitListener(queues = RabbitMQConfig.ANALYTICS_REG_QUEUE)
    public void handleRegisteredAnalytics(String message, Channel channel, Message rawMessage) throws IOException {
        try {
            String[] parts = message.split("\\|");
            String login = parts[1];
            String email = parts[2];

            log.info("[ANALYTICS] Новый пользователь c логином {} и почтой {}", login, email);

            writeToFile(message);

            channel.basicAck(
                    rawMessage.getMessageProperties().getDeliveryTag(),
                    false
            );

        } catch (Exception e) {
            log.error("[ANALYTICS] Ошибка обработки user.registered", e);
            channel.basicNack(
                    rawMessage.getMessageProperties().getDeliveryTag(),
                    false,
                    false
            );
        }
    }

    /**
     * Подписчик на покупку билетов
     * */
    @RabbitListener(queues = RabbitMQConfig.ANALYTICS_PURCHASE_QUEUE)
    public void handleEmail(String message, Channel channel, Message rawMessage) throws IOException {
        try {
            String[] parts = message.split("\\|");
            Long userId = Long.parseLong(parts[0]);
            Long eventId = Long.parseLong(parts[1]);
            String userName = parts[2];
            String eventName = parts[3];
            int countTickets = Integer.parseInt(parts[4]);

            log.info("[ANALYTICS] Совршена новая покупка пользователем {} на меропритие {} c количеством билетов {}", userName, eventName, countTickets);

            writeToFile(message);

            channel.basicAck(rawMessage.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("[ANALYTICS] Ошибка обработки покупки билетов", e);
            channel.basicNack(rawMessage.getMessageProperties().getDeliveryTag(), false, false);
        }
    }

    /**
     * Подписчик на отмену билетов
     * */
    @RabbitListener(queues = RabbitMQConfig.ANALYTICS_CANCEL_QUEUE)
    public void handleCancel(String message, Channel channel, Message rawMessage) throws IOException {
        try {
            String[] parts = message.split("\\|");
            String userName = parts[0];
            String eventName = parts[1];

            String text = String.format(
                    "[Analytics] Отмена покупки билетов пользователем %s на меропритие %s",
                    userName,
                    eventName
            );

            log.info(text);
            channel.basicAck(rawMessage.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            log.error("[Analytics] Ошибка обработки отмены", e);
            channel.basicNack(
                    rawMessage.getMessageProperties().getDeliveryTag(),
                    false,
                    false
            );
        }
    }

    /**
     * Подписчик обновление события
     * */
    @RabbitListener(queues = RabbitMQConfig.ANALYTICS_UPDATE_QUEUE)
    public void handleUpdateEvent(String message, Channel channel, Message rawMessage) throws IOException {
        try {
            String[] parts = message.split("\\|");
            String eventName = parts[1];

            String text = String.format(
                    "[Analytics] Добавление / изменение меропрития %s",
                    eventName
            );

            log.info(text);


            channel.basicAck(rawMessage.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {

            log.error("[Analytics] Ошибка обработки ticket.analytic-update.queue", e);
            channel.basicNack(
                    rawMessage.getMessageProperties().getDeliveryTag(),
                    false,
                    false
            );
        }
    }

    private void writeToFile(String msg) {
        try {
            String line = LocalDateTime.now() +
                    msg +
                    System.lineSeparator();

            Path path = Path.of("analytics.txt");

            Files.writeString(
                    path,
                    line,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

        } catch (IOException e) {
            log.error("Ошибка записи в файл аналитики", e);
        }
    }
}
