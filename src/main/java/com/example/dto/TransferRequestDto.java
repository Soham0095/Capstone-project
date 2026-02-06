package com.example.dto;

public record TransferRequestDto(
        Integer fromAccountId,
        Integer toAccountId,
        Integer amount
) {
}
