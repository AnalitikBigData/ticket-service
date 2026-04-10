package com.example.ticketService.contracts;

import java.time.LocalDate;

/**
 * Ответ на получение пользователя по ID
 */
public class GetUserByIdResponse {
    /**
     * ID пользователя
     */
    private Long userId;

    /**
     * Фамилия пользователя
     */
    private String surnameUser;

    /**
     * Имя пользователя
     */
    private String nameUser;

    /**
     * Отчество пользователя
     */
    private String fatherNameUser;

    /**
     * Дата рождения пользователя
     */
    private LocalDate birthDateUser;

    /**
     * Город пользователя
     */
    private String cityUser;

    /**
     * Почта пользователя
     */
    private String emailUser;

    /**
     * Логин пользователя
     */
    private String loginUser;

    /**
     * Роль пользователя
     */
    private String roleUser;

    public GetUserByIdResponse(Long userId,
                               String surnameUser,
                               String nameUser,
                               String fatherNameUser,
                               LocalDate birthDateUser,
                               String cityUser,
                               String emailUser,
                               String loginUser,
                               String roleUser)
    {
        this.userId = userId;
        this.surnameUser = surnameUser;
        this.nameUser = nameUser;
        this.fatherNameUser = fatherNameUser;
        this.birthDateUser = birthDateUser;
        this.cityUser = cityUser;
        this.emailUser = emailUser;
        this.loginUser = loginUser;
        this.roleUser = roleUser;
    }

    /**
     * Геттер пользователя по ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Сеттер пользователя по ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

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
     * Геттер отчетсва пользователя
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
     * Геттер города пользователя
     */
    public String getCityUser() {
        return cityUser;
    }

    /**
     * Сеттер города пользователя
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
