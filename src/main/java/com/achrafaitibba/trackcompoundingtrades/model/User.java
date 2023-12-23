package com.achrafaitibba.trackcompoundingtrades.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Entity
@Component
public class User {
    @Id
    private String username;
    private String password;
    @OneToOne
    private Account account;
}
