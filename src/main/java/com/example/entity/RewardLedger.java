package com.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "RewardLedger")
public class RewardLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer accountId;
    private Integer transactionLogId;
    private Integer pointsEarned;
    private Integer transactionAmount;
    private LocalDateTime createdOn = LocalDateTime.now();

    public RewardLedger() {}

    public RewardLedger(Integer accountId, Integer transactionLogId,
                        Integer pointsEarned, Integer transactionAmount) {
        this.accountId = accountId;
        this.transactionLogId = transactionLogId;
        this.pointsEarned = pointsEarned;
        this.transactionAmount = transactionAmount;
    }

    public Integer getId() { return id; }

    public Integer getAccountId() { return accountId; }
    public void setAccountId(Integer accountId) { this.accountId = accountId; }

    public Integer getTransactionLogId() { return transactionLogId; }
    public void setTransactionLogId(Integer transactionLogId) { this.transactionLogId = transactionLogId; }

    public Integer getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(Integer pointsEarned) { this.pointsEarned = pointsEarned; }

    public Integer getTransactionAmount() { return transactionAmount; }
    public void setTransactionAmount(Integer transactionAmount) { this.transactionAmount = transactionAmount; }

    public LocalDateTime getCreatedOn() { return createdOn; }
    public void setCreatedOn(LocalDateTime createdOn) { this.createdOn = createdOn; }

    @Override
    public String toString() {
        return "RewardLedger{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", transactionLogId=" + transactionLogId +
                ", pointsEarned=" + pointsEarned +
                ", transactionAmount=" + transactionAmount +
                ", createdOn=" + createdOn +
                '}';
    }
}
