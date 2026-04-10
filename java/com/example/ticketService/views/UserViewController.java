package com.example.ticketService.views;

import com.example.ticketService.contracts.LoginUserRequest;
import com.example.ticketService.contracts.RegisterUserRequest;
import com.example.ticketService.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserViewController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new RegisterUserRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") RegisterUserRequest request,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register"; // Если есть ошибки валидации, возвращаем на форму
        }
        userService.registerUser(request);
        return "redirect:/login?success"; // После успеха отправляем на вход
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginUserRequest());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@Valid @ModelAttribute("loginRequest") LoginUserRequest request,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "login"; // Если логин/пароль пустые
        }
        userService.LoginUser(request);
        return "redirect:/?auth_success";
    }


}
