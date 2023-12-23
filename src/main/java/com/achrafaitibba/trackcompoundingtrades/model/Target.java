package com.achrafaitibba.trackcompoundingtrades.model;

import com.achrafaitibba.trackcompoundingtrades.enumeration.TimeFrame;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Component
public class Target {

    @Id
    private Long targetId;
    private TimeFrame timeFrame;
    private Date startDate;
    private Date endDate;
    private Double estimatedBalanceByTargetAndTimeFrame;
    private Double variableEstimatedBalanceByTargetAndTimeFrame;


}
