package com.achrafaitibba.trackcompoundingtrades.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
    private Integer estimatedLossPossibilities;
    private Integer tradingCycle; // related to the estimated loss possibilities, eg: "estLossPo = 10", "trading cycle = 2" = losing 2 trades for every 10
    private Double stopLossPercentage;
    private Double currentBalance;
    private Double estimatedCompoundedBalance; // after finishing the trading period
    @Temporal(TemporalType.DATE)
    private LocalDate officialStartDate; // format : "yyyy-mm-dd"
    @OneToOne
    private CompoundingPeriod compoundingPeriod;

}
