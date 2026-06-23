package com.example.dto;

import java.util.List;

public record RewardSummaryDto(
        Integer totalPoints,
        List<RewardLedgerDto> history
) {}
