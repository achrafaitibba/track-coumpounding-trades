package com.achrafaitibba.trackcompoundingtrades.dto;

public record AccountStats(
        Double currentBalance,
        Double ATP, // current balance - base
        Double mainTarget, // est final target
        Double estimatedATP, // est final target - base
        Integer totalTrades // count(trades)
) {
}
