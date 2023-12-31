package com.achrafaitibba.trackcompoundingtrades.dto.request;


import java.time.LocalDate;

public record AccountRegisterRequest(
        String username,
        String password,
        Double baseCapital,
        Double compoundPercentage,
        Double estimatedFeesByTradePercentage,
        Integer estimatedLossPossibilities,
        Integer tradingCycle,
        Double stopLossPercentage,
        LocalDate officialStartDate,
        CompoundingPeriodRequest compoundingPeriod,
        String securityAnswer
) {
}
