package com.example.ticketService.contracts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Запрос на логин пользователя
 */
public class LoginUserRequest {
    /**
     * Логин пользователя
     */
    @NotBlank(message = "Логин обязателен ")
    @Size(min = 3, max = 15, message = "Логин должен быть 3-15 символов")
    private String loginUser;

    /**
     * Пароль пользователя
     */
    @NotBlank(message = "Пароль обязателен")
    private String passwordUser;

    /**
     * Геттер логина пользователя
     */
    public String getLoginUser() {
        return loginUser;
    }

    /**
     * Сеттер логина пользователя
     */
    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    /**
     * Геттер пароля пользователя
     */
    public String getPasswordUser() {
        return passwordUser;
    }

    /**
     * Сеттер пароля пользователя
     */
    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }
}
