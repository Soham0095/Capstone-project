package com.example.service;

import com.example.dto.RewardSummaryDto;
import com.example.dto.TransferRequestDto;

public interface RewardService {

    /**
     * Grants reward points to the sender for a successful transfer.
     * No-op if: self-transfer, or floor(amount/100) == 0.
     *
     * @param dto              the original transfer request
     * @param transactionLogId the ID of the already-saved TransactionLog row
     */
    void grantReward(TransferRequestDto dto, Integer transactionLogId);

    /**
     * Returns the total reward points and full history for the given account.
     */
    RewardSummaryDto getRewardSummary(Integer accountId);
}
