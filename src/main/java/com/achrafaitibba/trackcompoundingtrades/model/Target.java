package com.achrafaitibba.trackcompoundingtrades.model;

import com.achrafaitibba.trackcompoundingtrades.enumeration.TimeFrame;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Component
public class Target {

    @Id
    @GeneratedValue
    private Long targetId;
    private TimeFrame timeFrame;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double estimatedBalanceByTargetAndTimeFrame;
    private Double variableEstimatedBalanceByTargetAndTimeFrame; //todo current balance


}
