package com.example.controller;

import com.example.entity.Account;
import com.example.entity.RewardLog;
import com.example.repository.RewardLogRepository;
import com.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
public class RewardController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RewardLogRepository rewardLogRepository;

    @GetMapping("/accounts/{id}/rewards")
    public ResponseEntity<List<RewardLog>> getRewardHistory(@PathVariable int id) {
        // Validate account existence
        accountService.getAccountById(id);
        
        List<RewardLog> history = rewardLogRepository.findByAccountIdOrderByCreatedOnDesc(id);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/accounts/{id}/reward-points")
    public ResponseEntity<Map<String, Object>> getRewardPoints(@PathVariable int id) {
        // Validate account existence and retrieve points
        Account account = accountService.getAccountById(id);
        
        return ResponseEntity.ok(Map.of(
                "accountId", id,
                "rewardPoints", account.getRewardPoints() == null ? 0 : account.getRewardPoints()
        ));
    }

    @PostMapping("/accounts/{id}/redeem")
    public ResponseEntity<Map<String, Object>> redeemPoints(@PathVariable int id, @RequestBody(required = false) Map<String, Integer> body) {
        Account account = accountService.getAccountById(id);

        int available = account.getRewardPoints() == null ? 0 : account.getRewardPoints();
        int pointsToRedeem = available;
        if (body != null && body.get("points") != null) {
            pointsToRedeem = body.get("points");
        }

        if (pointsToRedeem <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Points to redeem must be greater than zero"));
        }

        if (pointsToRedeem > available) {
            return ResponseEntity.badRequest().body(Map.of("error", "Insufficient reward points"));
        }

        // Simple conversion: 1 point = 1 unit of currency
        int amountCredit = pointsToRedeem;

        account.setRewardPoints(available - pointsToRedeem);
        account.setBalance((account.getBalance() == null ? 0 : account.getBalance()) + amountCredit);

        String description = "Redeemed " + pointsToRedeem + " points for Rs. " + amountCredit;
        RewardLog log = new RewardLog(account.getId(), -pointsToRedeem, description);
        rewardLogRepository.save(log);

        accountService.updateAccount(account);

        return ResponseEntity.ok(Map.of(
                "accountId", id,
                "redeemedPoints", pointsToRedeem,
                "rewardPoints", account.getRewardPoints(),
                "balance", account.getBalance()
        ));
    }
}
