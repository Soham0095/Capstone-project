package com.example.dto;

public record CreateAccountRequest(
        String username,
        String password,
        String holderName
) {
}
