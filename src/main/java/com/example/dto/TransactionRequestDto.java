package com.example.dto;

public record TransactionRequestDto(
        Integer accountId,
        Integer amount,
        String action
) {
}
