package com.example.service;

import com.example.dto.RewardLedgerDto;
import com.example.dto.RewardSummaryDto;
import com.example.dto.TransferRequestDto;
import com.example.entity.RewardLedger;
import com.example.repository.RewardLedgerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("rewardService")
public class RewardServiceImpl implements RewardService {

    @Autowired
    private RewardLedgerRepository rewardLedgerRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public void grantReward(TransferRequestDto dto, Integer transactionLogId) {

        // Rule 1: No self-transfers
        if (dto.fromAccountId().equals(dto.toAccountId())) {
            System.out.println("Reward skipped: self-transfer");
            return;
        }

        // Rule 2: Floor division — every full ₹100 earns 1 point
        int points = dto.amount() / 100;
        if (points == 0) {
            System.out.println("Reward skipped: amount " + dto.amount() + " is below ₹100 threshold");
            return;
        }

        // Rule 3: Write audit entry to RewardLedger only (no Account table touched)
        RewardLedger entry = new RewardLedger(
                dto.fromAccountId(),
                transactionLogId,
                points,
                dto.amount()
        );
        rewardLedgerRepository.save(entry);

        System.out.println("Reward granted: " + points + " point(s) to account " + dto.fromAccountId());
    }

    @Override
    public RewardSummaryDto getRewardSummary(Integer accountId) {
        // Validate account exists (throws AccountNotFoundException if not)
        accountService.getAccountById(accountId);

        // Aggregate total points directly from RewardLedger — no Account column needed
        Integer totalPoints = rewardLedgerRepository.sumPointsByAccountId(accountId);

        List<RewardLedger> logs = rewardLedgerRepository.findByAccountIdOrderByCreatedOnDesc(accountId);

        List<RewardLedgerDto> history = logs.stream()
                .map(log -> new RewardLedgerDto(
                        log.getId(),
                        log.getPointsEarned(),
                        log.getTransactionAmount(),
                        log.getTransactionLogId(),
                        log.getCreatedOn()
                ))
                .collect(Collectors.toList());

        return new RewardSummaryDto(totalPoints, history);
    }
}
