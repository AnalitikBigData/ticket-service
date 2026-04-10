package com.example.ticketService.services;

import com.example.ticketService.configs.RabbitMQConfig;
import com.example.ticketService.contracts.GetUserByIdResponse;
import com.example.ticketService.contracts.LoginUserRequest;
import com.example.ticketService.contracts.RegisterUserRequest;
import com.example.ticketService.entities.User;
import com.example.ticketService.repositories.TicketRepository;
import com.example.ticketService.repositories.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final RabbitTemplate rabbitTemplate;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       TicketRepository ticketRepository,
                       PasswordEncoder passwordEncoder,
                       RabbitTemplate rabbitTemplate)
    {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.passwordEncoder = passwordEncoder;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Регистрация пользователя
     * */
    @Transactional
    public void registerUser(RegisterUserRequest request) {

        if (userRepository.findByLoginUser(request.getLoginUser()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        //todo в маппер
        User user = new User();
        user.setLoginUser(request.getLoginUser());
        user.setEmailUser(request.getEmailUser());
        user.setNameUser(request.getNameUser());
        user.setSurnameUser(request.getSurnameUser());
        user.setFatherNameUser(request.getFatherNameUser());
        user.setBirthDateUser(request.getBirthDateUser());
        user.setCityUser(request.getCityUser());
        user.setRoleUser(request.getRoleUser());

        user.setPasswordUser(passwordEncoder.encode(request.getPasswordUser()));

        userRepository.save(user);

        String message = String.format("%d|%s|%s",
                user.getUserId(),
                user.getLoginUser(),
                user.getEmailUser()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.USER_REGISTERED_TOPIC,
                message
        );
    }

    /**
     * Логин пользователя
     * */
    //TODO добавить токены (пока упрощенная реализация)
    public Boolean LoginUser(LoginUserRequest request){
        var user = userRepository.findByLoginUser(request.getLoginUser())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return passwordEncoder.matches(request.getPasswordUser(), user.getPasswordUser());
    }

    /**
     * Получение пользовтаеля по ID
     * */
    public GetUserByIdResponse getUserById(Long id){
        return userRepository.findByUserId(id)
                .map(this::mapGetUserByIdResponse)
                .orElse(null);
    }

    /**
     * Аналитика для админов
     * */
    public void analytics(){
        var registered = userRepository.countUsers();
        var sold = ticketRepository.countSoldTicketsByMonth();
        var total = ticketRepository.getRevenueByMonth();
        String message = "Registered=" + registered +
                        "; Sold=" + sold +
                        "; Revenue=" + total;

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ANALYTICS_QUEUE,
                message
        );
    }

    // маппинги
    private GetUserByIdResponse mapGetUserByIdResponse(User user){
        return new GetUserByIdResponse(
                user.getUserId(),
                user.getSurnameUser(),
                user.getNameUser(),
                user.getFatherNameUser(),
                user.getBirthDateUser(),
                user.getCityUser(),
                user.getEmailUser(),
                user.getLoginUser(),
                user.getRoleUser()
        );
    }
}
