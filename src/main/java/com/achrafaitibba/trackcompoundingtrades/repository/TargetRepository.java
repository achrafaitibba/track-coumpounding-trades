package com.achrafaitibba.trackcompoundingtrades.repository;

import com.achrafaitibba.trackcompoundingtrades.enumeration.TimeFrame;
import com.achrafaitibba.trackcompoundingtrades.model.Target;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TargetRepository extends JpaRepository<Target, Long> {
    Page<Target> findByTimeFrameAndAccount_AccountId(TimeFrame timeFrame, UUID account_accountId, Pageable pageable);
}
