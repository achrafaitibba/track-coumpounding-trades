package com.achrafaitibba.trackcompoundingtrades.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Data
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
    private Double compoundStopLossPercentage;
    private Double currentBalance;
    private Double estimatedCompoundedBalance; // after finishing the trading period
    @OneToOne
    private CompoundingPeriod compoundingPeriod;
    @OneToMany
    private List<Target> targets;
}
