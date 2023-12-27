package com.achrafaitibba.trackcompoundingtrades.dto.request;

import com.achrafaitibba.trackcompoundingtrades.model.CompoundingPeriod;

import java.time.LocalDate;
import java.util.Date;

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
        CompoundingPeriodRequest compoundingPeriod
) {
}
