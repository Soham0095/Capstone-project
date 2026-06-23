package com.example.controller;

import com.example.dto.RewardSummaryDto;
import com.example.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RewardController {

    @Autowired
    private RewardService rewardService;

    // Get reward summary (total points + history) for a given account
    @GetMapping("/rewards/{accountId}")
    public RewardSummaryDto getRewardSummary(@PathVariable Integer accountId) {
        return rewardService.getRewardSummary(accountId);
    }
}
