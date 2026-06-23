package com.example.aspect;

import com.example.dto.TransferRequestDto;
import com.example.entity.TransactionLog;
import com.example.enums.TransactionStatus;
import com.example.repository.TransactionLogRepository;
import com.example.service.RewardService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TransactionLoggingAspect {

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    @Autowired
    private RewardService rewardService;

    @AfterReturning(pointcut = "execution(* com.example.service.TransferService.transfer(..))")
    public void logTransaction(JoinPoint joinPoint) {
        // Save the log and get back the persisted entity (with its generated ID)
        TransactionLog saved = saveLog(joinPoint, "SUCCESS", null);

        // Wire in reward grant — isolated in try-catch so a reward failure
        // never propagates back and masks a successful transfer to the user
        if (saved != null) {
            Object[] args = joinPoint.getArgs();
            if (args.length > 0 && args[0] instanceof TransferRequestDto dto) {
                try {
                    rewardService.grantReward(dto, saved.getId());
                } catch (Exception e) {
                    System.err.println("Reward grant failed (transfer was successful): " + e.getMessage());
                }
            }
        }
    }

    @AfterThrowing(pointcut = "execution(* com.example.service.TransferService.transfer(..))", throwing = "ex")
    public void logTransactionException(JoinPoint joinPoint, Exception ex) {
        saveLog(joinPoint, "FAILED", ex.getMessage());
        // No reward on failure — nothing else to do here
    }

    /**
     * Persists a TransactionLog row and returns the saved entity (with generated ID).
     * Returns null if the join point args don't match the expected shape.
     */
    private TransactionLog saveLog(JoinPoint joinPoint, String status, String failureReason) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof TransferRequestDto transferRequestDto) {
            TransactionLog transactionLog = new TransactionLog(
                    transferRequestDto.fromAccountId(),
                    transferRequestDto.toAccountId(),
                    transferRequestDto.amount()
            );
            transactionLog.setStatus(TransactionStatus.valueOf(status));
            transactionLog.setFailureReason(failureReason);
            return transactionLogRepository.save(transactionLog);
        }
        return null;
    }
}
