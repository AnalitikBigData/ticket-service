package com.example.ticketService.contracts;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Запрос на отмену покупки по ID
 * */
public class CancelPurchaseByIdRequest {
    /**
     * ID покупки
     * */
    @NotNull(message = "Purchase ID обязателен")
    private Long purchaseId;

    /**
     * Количество билетов
     * */
    @NotNull(message = "Количество билетов для отмены обязательно")
    @Positive(message = "Кол-во билетов должно быть больше 0")
    private Integer count;

    /**
     * Конструктор
     * */
    public CancelPurchaseByIdRequest(Long purchaseId, Integer count){
        this.purchaseId = purchaseId;
        this.count = count;
    }

    /**
     * Геттер ID покупки
     * */
    public Long getPurchaseId() {
        return purchaseId;
    }

    /**
     * Сеттер ID покупки
     * */
    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    /**
     * Геттер кол-ва билетов
     * */
    public Integer getCount() {
        return count;
    }

    /**
     * Сеттер кол-ва билетов
     * */
    public void setCount(Integer count) {
        this.count = count;
    }
}
