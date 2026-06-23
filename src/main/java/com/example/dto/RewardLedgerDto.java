package com.example.dto;

import java.time.LocalDateTime;

public record RewardLedgerDto(
        Integer id,
        Integer pointsEarned,
        Integer transactionAmount,
        Integer transactionLogId,
        LocalDateTime createdOn
) {}
