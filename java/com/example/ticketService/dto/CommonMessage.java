package com.example.ticketService.dto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Общесистемное сообщение
 * */
public record CommonMessage(String type,
                            LocalDateTime createdAt,
                            Map<String, Object> data) { }
