package com.achrafaitibba.trackcompoundingtrades.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Component
public class Trade {

    @Id
    private UUID tradeId;
    private Date date;
    private Double investedCap;
    private Double closedAt;
    private Double profit;
    private Double targetByInvestedCap;
    private Double diffProfitTarget; // difference between target and profit > calc(target - closedAt)
    @OneToOne
    private Pair tradingPair;
    @ManyToOne
    private Account account;
}
