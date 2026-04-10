package com.example.ticketService.RabbitMQService;

import com.example.ticketService.configs.RabbitMQConfig;
import com.example.ticketService.entities.Constants;
import com.example.ticketService.entities.Ticket;
import com.example.ticketService.entities.User;
import com.example.ticketService.repositories.TicketRepository;
import com.example.ticketService.repositories.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Подписчик на обновление события
 * */
@Component
public class RabbitMQEventUpdateConsumer {
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    private static final Logger log = LoggerFactory.getLogger(RabbitMQEventUpdateConsumer.class);

    public RabbitMQEventUpdateConsumer(UserRepository userRepository,
                                       TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.EVENT_UPDATE_QUEUE)
    public void handleEventUpdate(String message, Channel channel, Message rawMessage) throws IOException {
        try {
            String[] parts = message.split("\\|");
            Long eventId = Long.parseLong(parts[0]);
            String eventName = parts[1];
            String newDate = parts[2];

            List<User> admins = userRepository.findByRoleUser(Constants.RoleUser.ADMIN.toString());
            List<Ticket> tickets =
                    ticketRepository.findByEventEventIdAndStatus(
                            eventId,
                            Constants.TicketStatus.SOLD.toString()
                    );

            Set<String> users = tickets.stream()
                    .map(t -> t.getPurchase().getUser().getNameUser())
                    .collect(Collectors.toSet());

            for (String user : users) {
                String text = String.format(
                        "Уважаемый %s, мероприятие '%s' было изменено. Проверьте информацию.",
                        user,
                        eventName,
                        newDate
                );
                log.info(text);
            }

            for (User admin : admins) {
                String text = String.format(
                        "Администратор %s, было добавлено / изменено событие %s id %d",
                        admin.getLoginUser(),
                        eventName,
                        eventId
                );

                log.info(text);
            }

            channel.basicAck(rawMessage.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {

            log.error("Ошибка обработки event.updated", e);
            channel.basicNack(
                    rawMessage.getMessageProperties().getDeliveryTag(),
                    false,
                    false
            );
        }
    }
}


