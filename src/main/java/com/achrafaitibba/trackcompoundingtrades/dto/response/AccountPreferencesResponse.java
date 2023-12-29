package com.achrafaitibba.trackcompoundingtrades.dto.response;

import com.achrafaitibba.trackcompoundingtrades.model.CompoundingPeriod;

import java.time.LocalDate;

public record AccountPreferencesResponse(

        Double baseCapital,
        Double compoundPercentage,
        Double estimatedFeesByTradePercentage,
        Integer estimatedLossPossibilities,
        Integer tradingCycle,
        Double stopLossPercentage,
        LocalDate officialStartDate,
        CompoundingPeriod compoundingPeriod

) {
}
