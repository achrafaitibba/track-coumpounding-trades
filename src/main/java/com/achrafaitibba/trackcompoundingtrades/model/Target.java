package com.achrafaitibba.trackcompoundingtrades.model;

import com.achrafaitibba.trackcompoundingtrades.enumeration.TimeFrame;
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
public class Target {

    @Id
    @GeneratedValue
    private Long targetId;
    private TimeFrame timeFrame;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double estimatedBalanceByTargetAndTimeFrame;
    @ManyToOne
    @JsonIgnore
    private Account account;

}
