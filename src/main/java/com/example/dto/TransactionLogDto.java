package com.example.dto;

import com.example.enums.TransactionStatus;
import java.time.LocalDateTime;

public record TransactionLogDto(
        Integer id,
        Integer fromAccountId,
        String fromHolderName,
        Integer toAccountId,
        String toHolderName,
        Integer amount,
        TransactionStatus status,
        String failureReason,
        LocalDateTime createdOn
) {}
