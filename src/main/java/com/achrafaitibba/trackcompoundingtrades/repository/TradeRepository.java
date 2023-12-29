package com.achrafaitibba.trackcompoundingtrades.repository;

import com.achrafaitibba.trackcompoundingtrades.model.Trade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    Page<Trade> findByAccount_AccountId(UUID accountId, Pageable pageable);
    int deleteAllByAccount_AccountId(UUID accountId);

    Integer countAllByAccount_AccountId(UUID accountId);
}
