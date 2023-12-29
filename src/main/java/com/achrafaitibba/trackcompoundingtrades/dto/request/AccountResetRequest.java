package com.achrafaitibba.trackcompoundingtrades.dto.request;

import java.time.LocalDate;

public record AccountResetRequest(

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
