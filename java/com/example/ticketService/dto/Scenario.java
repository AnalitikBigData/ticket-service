package com.example.ticketService.dto;

import java.util.List;

public record Scenario(long delay,
                       List<Boolean> flags,
                       String pattern) {}
