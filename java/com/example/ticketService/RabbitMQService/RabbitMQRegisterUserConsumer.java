package com.example.ticketService.RabbitMQService;

import com.example.ticketService.configs.RabbitMQConfig;
import com.example.ticketService.entities.Constants;
import com.example.ticketService.entities.User;
import com.example.ticketService.repositories.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Подписчик на регистрацию пользователя
 * */
@Component
public class RabbitMQRegisterUserConsumer {

    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(RabbitMQRegisterUserConsumer.class);

    public RabbitMQRegisterUserConsumer(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.ADMIN_NOTIFY_QUEUE)
    public void handleUserRegistered(String message, Channel channel, Message rawMessage) throws IOException {
        try {
            String[] parts = message.split("\\|");
            String login = parts[1];
            String email = parts[2];

            List<User> admins = userRepository.findByRoleUser(Constants.RoleUser.ADMIN.toString());

            for (User admin : admins) {

                String text = String.format(
                        "Администратор %s, зарегистрирован новый пользователь: %s (%s)",
                        admin.getLoginUser(),
                        login,
                        email
                );

                log.info(text);
            }

            channel.basicAck(
                    rawMessage.getMessageProperties().getDeliveryTag(),
                    false
            );

        } catch (Exception e) {
            log.error("Ошибка обработки user.registered", e);
            channel.basicNack(
                    rawMessage.getMessageProperties().getDeliveryTag(),
                    false,
                    false
            );
        }
    }
}
