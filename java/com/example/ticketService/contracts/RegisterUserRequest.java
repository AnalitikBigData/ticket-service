package com.example.ticketService.contracts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Запрос на регистрацию пользователя
 */
public class RegisterUserRequest {
    /**
     * Фамилия пользователя
     */
    @NotBlank(message = "Фамилия обязательна")
    @Size(min = 1, max = 50, message = "Фамилия должна быть 1-50 символов")
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё-]+$",
            message = "Фамилия содержит только буквы")
    private String surnameUser;

    /**
     * Имя пользователя
     */
    @NotBlank(message = "Имя обязательно")
    @Size(min = 1, max = 50, message = "Имя должно быть 1-50 символов")
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё-]+$",
            message = "Имя содержит только буквы")
    private String nameUser;

    /**
     * Отчество пользователя
     */
    @Size(max = 50, message = "Отчество должно быть не более 50 символов")
    //@Pattern(regexp = "^[A-Za-zА-Яа-яЁё-]+$", message = "Отчество содержит только буквы")
    private String fatherNameUser;

    /**
     * Дата рождения пользователя
     */
    @Past(message = "Дата рождения должна быть меньше текущей")
    private LocalDate birthDateUser;

    /**
     * Город проживания пользователя
     */
    @Size(min = 2, max = 100)
    private String cityUser;

    /**
     * Почта пользователя
     */
    @NotBlank(message = "Почта должна быть заполнена")
    @Size(max = 25, message = "Максимальное кол-во сиволов в почте не превышает 25")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email формат: name@domain.com"
    )
    private String emailUser;

    /**
     * Логин пользователя
     */
    @NotBlank(message = "Логин должен быть заполнен")
    @Size(min = 3, max = 15)
    private String loginUser;

    /**
     * Пароль пользователя
     */
    @NotBlank(message = "Пароль обязателен для сохранения")
    private String passwordUser;

    /**
     * Роль пользователя
     */
    private String roleUser;

    /**
     * Геттер фамилии пользователя
     */
    public String getSurnameUser() {
        return surnameUser;
    }

    /**
     * Сеттер фамилии пользователя
     */
    public void setSurnameUser(String surnameUser) {
        this.surnameUser = surnameUser;
    }

    /**
     * Геттер имени пользователя
     */
    public String getNameUser() {
        return nameUser;
    }

    /**
     * Сеттер имени пользователя
     */
    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    /**
     * Геттер отчества пользователя
     */
    public String getFatherNameUser() {
        return fatherNameUser;
    }

    /**
     * Сеттер отчества пользователя
     */
    public void setFatherNameUser(String fatherNameUser) {
        this.fatherNameUser = fatherNameUser;
    }

    /**
     * Геттер даты рождения пользователя
     */
    public LocalDate getBirthDateUser() {
        return birthDateUser;
    }

    /**
     * Сеттер даты рождения пользователя
     */
    public void setBirthDateUser(LocalDate birthDateUser) {
        this.birthDateUser = birthDateUser;
    }

    /**
     * Геттер города проживания пользователя
     */
    public String getCityUser() {
        return cityUser;
    }

    /**
     * Сеттер города проживания пользователя
     */
    public void setCityUser(String cityUser) {
        this.cityUser = cityUser;
    }

    /**
     * Геттер почты пользователя
     */
    public String getEmailUser() {
        return emailUser;
    }

    /**
     * Сеттер почты пользователя
     */
    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

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
     * Сеттер пользователя
     */
    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    /**
     * Геттер роли пользователя
     */
    public String getRoleUser() {
        return roleUser;
    }

    /**
     * Сеттер роли пользователя
     */
    public void setRoleUser(String roleUser) {
        this.roleUser = roleUser;
    }
}
