package com.achrafaitibba.trackcompoundingtrades.model;

import com.achrafaitibba.trackcompoundingtrades.enumeration.TimeFrame;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class CompoundingPeriod {
    @Id
    @GeneratedValue
    private Long periodId;
    private Integer number;
    private TimeFrame timeFrame;
}
