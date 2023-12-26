package com.achrafaitibba.trackcompoundingtrades.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Component
public class Account {
    @Id
    @GeneratedValue
    private UUID accountId;
    private Double baseCapital;
    private Double compoundPercentage;
    private Double estimatedFeesByTradePercentage;
    private Integer estimatedLossPossibilitiesPercentage; // should be something like 10, 20, 30...
    private Double stopLossPercentage;
    private Double currentBalance;
    private Double estimatedCompoundedBalance; // after finishing the trading period
    @Temporal(TemporalType.DATE)
    private Date officialStartDate; // format : "yyyy-mm-dd"
    // CompoundingPeriod =
    // Official start date + (number * timeframe) > use calendar date to calculate, not numbers
    @OneToOne
    private CompoundingPeriod compoundingPeriod;
    @OneToMany
    private List<Target> targets;
}
