package com.example.entity;

import com.example.enums.TransactionStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="TransactionLog")
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer fromAccountId;
    private Integer toAccountId;
    private Integer amount;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private String failureReason = "";
    private String idempotencyKey = "";
    private LocalDateTime createdOn = LocalDateTime.now();

    // Constructors

    public TransactionLog() {
    }
    public TransactionLog(Integer fromAccountId, Integer toAccountId, Integer amount) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    // Getter Setter methods
    public Integer getId() {
        return id;
    }
    public Integer getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Integer fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Integer getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Integer toAccountId) {
        this.toAccountId = toAccountId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    @Override
    public String toString() {
        return "TransactionLog{" +
                "id=" + id +
                ", fromAccountId=" + fromAccountId +
                ", toAccountId=" + toAccountId +
                ", amount=" + amount +
                ", status=" + status +
                ", failureReason='" + failureReason + '\'' +
                ", idempotencyKey='" + idempotencyKey + '\'' +
                ", createdOn=" + createdOn +
                '}';
    }
}
