package com.example.aspect;

import com.example.enums.TransactionStatus;
import com.example.repository.TransactionLogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
public class TransactionLoggingAspect {

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    @AfterReturning(pointcut = "execution(* com.example.service.TransferService.transfer(..))||" +
    "execution(* com.example.service.AccountService.updateBalance(..))")
    public void logTransaction(JoinPoint joinPoint) {
        saveLog(joinPoint, "SUCCESS",null);
    }

    @AfterThrowing(pointcut = "execution(* com.example.service.TransferService.transfer(..)) ||"+
            "execution(* com.example.service.AccountService.updateBalance(..))", throwing = "ex")
    public void logTransactionException(JoinPoint joinPoint, Exception ex) {
        saveLog(joinPoint, "FAILED", ex.getMessage());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW )
    public void saveLog(JoinPoint joinPoint, String status, String failure_reason) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof com.example.dto.TransferRequestDto transferRequestDto) {
            com.example.entity.TransactionLog transactionLog = new com.example.entity.TransactionLog(
                    transferRequestDto.fromAccountId(),
                    transferRequestDto.toAccountId(),
                    transferRequestDto.amount()
            );
            transactionLog.setStatus(TransactionStatus.valueOf(status));
            transactionLog.setFailureReason(failure_reason);
            transactionLogRepository.save(transactionLog);

        }

    }


}


