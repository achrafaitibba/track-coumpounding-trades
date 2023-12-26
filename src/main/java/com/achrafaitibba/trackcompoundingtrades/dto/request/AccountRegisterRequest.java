package com.achrafaitibba.trackcompoundingtrades.dto.request;

import com.achrafaitibba.trackcompoundingtrades.model.CompoundingPeriod;

import java.util.Date;

public record AccountRegisterRequest(
        String username,
        String password,
        Double baseCapital,
        Double compoundPercentage,
        Double estimatedFeesByTradePercentage,
        Integer estimatedLossPossibilitiesPercentage,
        Double stopLossPercentage,
        Date officialStartDate,
        CompoundingPeriodRequest compoundingPeriod
) {
}
