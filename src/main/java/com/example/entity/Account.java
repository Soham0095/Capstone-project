package com.example.entity;

import com.example.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
@Entity
@Table(name="Account")
@Setter
@Getter
@NoArgsConstructor

public class Account {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "holderName")
    private String holderName;
    private Integer balance = 0;
    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.active;
    private Long version = 0L;
    @Column(name = "lastUpdated")
    private LocalDateTime lastUpdated = LocalDateTime.now();
    private String username;
    private String password;
    @Column(name = "rewardPoints")
    private Integer rewardPoints = 0;

    // setter getter methods

    public Integer getId() {
        return id;
    }

    public String getHolderName() {
        return holderName;
    }

    public Integer getBalance() {
        return balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public Long getVersion() {
        return version;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {return password;}


    public void setId(Integer id) {
        this.id = id;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setpassword(String password) { this.password=password;}
    // This method is a placeholder for password hashing logic??

    public Integer getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(Integer rewardPoints) {
        this.rewardPoints = rewardPoints;
    }


    @Override
    public String toString() {
        return "Account{" +
                "id=" + id  +
                ", holderName='" + holderName + '\'' +
                ", balance=" + balance +
                ", status=" + status +
                ", version=" + version +
                ", lastUpdated=" + lastUpdated +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", rewardPoints=" + rewardPoints +
                '}';
    }


}

