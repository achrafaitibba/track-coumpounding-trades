package com.achrafaitibba.trackcompoundingtrades.repository;

import com.achrafaitibba.trackcompoundingtrades.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {
}
