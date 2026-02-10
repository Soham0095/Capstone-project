package com.example.dto;

public record CreateAccountRequest(
        Integer id,
        String holderName,
        String username
) {
}
