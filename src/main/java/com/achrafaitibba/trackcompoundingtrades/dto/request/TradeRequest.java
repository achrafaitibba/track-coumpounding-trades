package com.achrafaitibba.trackcompoundingtrades.dto.request;


import java.time.LocalDate;

public record TradeRequest(
        LocalDate tradeDate,
        Double investedCap,
        Double closedAt,
        String baseCoin,
        String quoteCoin

) {
}
