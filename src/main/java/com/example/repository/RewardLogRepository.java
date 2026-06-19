package com.example.repository;

import com.example.entity.RewardLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RewardLogRepository extends JpaRepository<RewardLog, Integer> {
    List<RewardLog> findByAccountIdOrderByCreatedOnDesc(Integer accountId);
}
