package com.achrafaitibba.trackcompoundingtrades.model;

import com.achrafaitibba.trackcompoundingtrades.enumeration.timeFrame;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class compoundingPeriod {
    @Id
    @GeneratedValue
    private Long periodId;
    private Integer number;
    private timeFrame timeFrame;
}
