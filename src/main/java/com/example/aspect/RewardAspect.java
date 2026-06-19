package com.example.aspect;

import com.example.dto.TransferRequestDto;
import com.example.entity.Account;
import com.example.entity.RewardLog;
import com.example.repository.RewardLogRepository;
import com.example.service.AccountService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
public class RewardAspect {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RewardLogRepository rewardLogRepository;

    @AfterReturning(pointcut = "execution(* com.example.service.TransferService.transfer(..))")
    @Transactional
    public void calculateAndApplyReward(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof TransferRequestDto transferRequestDto) {
            // Fetch sender and receiver account information
            Account fromAccount = accountService.getAccountById(transferRequestDto.fromAccountId());
            Account toAccount = accountService.getAccountById(transferRequestDto.toAccountId());

            boolean isSelfTransfer = fromAccount.getId().equals(toAccount.getId());
            boolean isDifferentUser = fromAccount.getUsername() != null && toAccount.getUsername() != null 
                    && !fromAccount.getUsername().equals(toAccount.getUsername());

            if (transferRequestDto.amount() > 100 && !isSelfTransfer && isDifferentUser) {
                int points = transferRequestDto.amount() / 100;
                if (points > 0) {
                    fromAccount.setRewardPoints((fromAccount.getRewardPoints() == null ? 0 : fromAccount.getRewardPoints()) + points);
                    String description = "Earned " + points + " reward points for transferring Rs. " + transferRequestDto.amount() + " to account ID " + toAccount.getId() + " (" + toAccount.getHolderName() + ")";
                    RewardLog rewardLog = new RewardLog(fromAccount.getId(), points, description);
                    
                    rewardLogRepository.save(rewardLog);
                    accountService.updateAccount(fromAccount);
                }
            }
        }
    }
}
