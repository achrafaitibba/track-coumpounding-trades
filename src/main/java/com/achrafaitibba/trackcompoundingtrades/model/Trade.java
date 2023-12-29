package com.achrafaitibba.trackcompoundingtrades.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Component
public class Trade {

    @Id
    @GeneratedValue
    private Long tradeId;
    private LocalDate date;
    private Double investedCap;
    private Double closedAt;
    private Double PNL; // (closedAt - investedCap)
    private Double targetByInvestedCap; // calculated for each trade
    private Double diffProfitTarget; // difference between target and profit > calc(target - closedAt)
    private String tradingPair;
    @ManyToOne
    @JsonIgnore
    private Account account;
}
