package com.achrafaitibba.trackcompoundingtrades.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Data
@NoArgsConstructor
@Entity
@Component
public class account implements UserDetails {
    @Id
    @GeneratedValue
    private Long accountId;
    private String username;
    private String password;
    private Double baseCapital;
    private Double compoundPercentage;
    private Double estimatedFeesByTradePercentage;
    private Integer estimatedLossPossibilitiesPercentage; // should be something like 10, 20, 30...
    private Double compoundStopLossPercentage;
    @OneToOne
    private compoundingPeriod compoundingPeriod;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
