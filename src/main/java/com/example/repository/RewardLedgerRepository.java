package com.example.repository;

import com.example.entity.RewardLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardLedgerRepository extends JpaRepository<RewardLedger, Integer> {

    List<RewardLedger> findByAccountIdOrderByCreatedOnDesc(Integer accountId);

    @Query("SELECT COALESCE(SUM(r.pointsEarned), 0) FROM RewardLedger r WHERE r.accountId = :accountId")
    Integer sumPointsByAccountId(Integer accountId);
}
