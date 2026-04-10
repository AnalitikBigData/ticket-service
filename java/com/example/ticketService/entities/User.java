package com.example.ticketService.entities;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Пользователь
 * */
@Entity
@Table(name = "USERS",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "EmailUser"),
                @UniqueConstraint(columnNames = "LoginUser")
        })
public class User {

    /**
     * ID пользователя
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USERID")
    private Long userId;

    /**
     * Фамилия пользователя
     * */
    @Column(name = "SURNAMEUSER", nullable = false, length = 100)
    private String surnameUser;

    /**
     * Имя пользователя
     * */
    @Column(name = "NAMEUSER", nullable = false, length = 100)
    private String nameUser;

    /**
     * Отчество пользователя
     * */
    @Column(name = "FATHERNAMEUSER", length = 100)
    private String fatherNameUser;

    /**
     * Дата рождения пользователя
     * */
    @Column(name = "BIRTHDATEUSER", nullable = false)
    private LocalDate birthDateUser;

    /**
     * Город пользовтаеля
     * */
    @Column(name = "CITYUSER", length = 100)
    private String cityUser;

    /**
     * Почта пользователя
     * */
    @Column(name = "EMAILUSER", nullable = false, length = 150)
    private String emailUser;

    /**
     * Логин пользователя
     * */
    @Column(name = "LOGINUSER", nullable = false, length = 100)
    private String loginUser;

    /**
     * Пароль почта
     * */
    @Column(name = "PASSWORDUSER", nullable = false, length = 255)
    private String passwordUser;

    /**
     * Роль пользователя
     * */
    @Column(name = "ROLE", nullable = false, length = 15)
    private String roleUser;

    /**
     * Покупки пользователя
     * */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Purchase> purchases;

    /**
     * Геттер ID пользователя
     * */
    public Long getUserId() {
        return userId;
    }

    /**
     * Сеттер ID пользователя
     * */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Геттер фамилии пользователя
     * */
    public String getSurnameUser() {
        return surnameUser;
    }

    /**
     * Сеттер фамилии пользователя
     * */
    public void setSurnameUser(String surnameUser) {
        this.surnameUser = surnameUser;
    }

    /**
     * Геттер имени пользвоателя
     * */
    public String getNameUser() {
        return nameUser;
    }

    /**
     * Сеттер имени пользовтаеля
     * */
    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    /**
     * Геттер отчества пользовтаеля
     * */
    public String getFatherNameUser() {
        return fatherNameUser;
    }

    /**
     * Сеттер отчества пользователя
     * */
    public void setFatherNameUser(String fatherNameUser) {
        this.fatherNameUser = fatherNameUser;
    }

    /**
     * Геттер даты рождения пользователя
     * */
    public LocalDate getBirthDateUser() {
        return birthDateUser;
    }

    /**
     * Сеттер даты рождения пользователя
     * */
    public void setBirthDateUser(LocalDate birthDateUser) {
        this.birthDateUser = birthDateUser;
    }

    /**
     * Геттер города проживания пользователя
     * */
    public String getCityUser() {
        return cityUser;
    }

    /**
     * Сеттер города проживания пользователя
     * */
    public void setCityUser(String cityUser) {
        this.cityUser = cityUser;
    }

    /**
     * Геттер почты пользователя
     * */
    public String getEmailUser() {
        return emailUser;
    }

    /**
     * Сеттер почты пользователя
     * */
    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    /**
     * Геттер логина пользователя
     * */
    public String getLoginUser() {
        return loginUser;
    }

    /**
     * Сеттер логина пользователя
     * */
    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    /**
     * Геттер пароля пользователя
     * */
    public String getPasswordUser() {
        return passwordUser;
    }

    /**
     * Сеттер пароля
     * */
    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    /**
     * Геттер роли пользователя
     * */
    public String getRoleUser() {
        return roleUser;
    }

    /**
     * Сеттер роли пользовтеля
     * */
    public void setRoleUser(String roleUser) {
        this.roleUser = roleUser;
    }

}
