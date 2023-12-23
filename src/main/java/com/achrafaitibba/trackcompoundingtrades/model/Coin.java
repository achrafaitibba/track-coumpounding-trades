package com.achrafaitibba.trackcompoundingtrades.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Data
@NoArgsConstructor
@Entity
@Component
public class Coin {
    @Id
    private String coinName;
}
