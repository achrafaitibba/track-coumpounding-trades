package com.achrafaitibba.trackcompoundingtrades.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Data
@NoArgsConstructor
@Entity
@Component
public class Pair {
    @Id
    @GeneratedValue
    private Integer pairId;
    @ManyToOne
    private Coin baseCoin;
    @ManyToOne
    private Coin quoteCoin;

}
