package com.achrafaitibba.trackcompoundingtrades.dto.request;

import com.achrafaitibba.trackcompoundingtrades.enumeration.TimeFrame;

public record CompoundingPeriodRequest(
        Integer number,
        TimeFrame timeFrame

) {
}
