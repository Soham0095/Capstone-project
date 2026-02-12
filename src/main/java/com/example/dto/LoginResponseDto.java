package com.example.dto;

import com.example.entity.Account;

public record LoginResponseDto (String message, boolean ok, Account body) {
}
