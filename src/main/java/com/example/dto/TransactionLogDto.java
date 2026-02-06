package com.example.dto;

public record TransactionLogDto(
        Integer fromAccountId,
        Integer toAccountId,
        Integer amount
) { }
