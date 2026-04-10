package com.example.ticketService.contracts;

/**
 * Ошибочный ответ
 * */
public class ErrorResponse extends Response{

    /**
     * Сообщение об ошибке
     * */
    private String message;

    /**
     * Геттер сообщения об ошибке
     * */
    public String getMessage() {
        return message;
    }

    /**
     * Сеттер сообщения об ошибке
     * */
    public void setMessage(String message) {
        this.message = message;
    }
}
