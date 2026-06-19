package com.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="RewardLog")
public class RewardLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "accountId")
    private Integer accountId;

    private Integer points;

    private String description;

    @Column(name = "createdOn")
    private LocalDateTime createdOn = LocalDateTime.now();

    // Constructors
    public RewardLog() {
    }

    public RewardLog(Integer accountId, Integer points, String description) {
        this.accountId = accountId;
        this.points = points;
        this.description = description;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "RewardLog{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", points=" + points +
                ", description='" + description + '\'' +
                ", createdOn=" + createdOn +
                '}';
    }
}
