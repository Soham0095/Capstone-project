package com.example.repository;

import com.example.entity.TransactionLog;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Integer> {

    @Query("SELECT t FROM TransactionLog t WHERE t.fromAccountId = :userId OR t.toAccountId = :userId")
    public List<TransactionLog> findByUserId(Integer userId);
}

