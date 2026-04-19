package com.example.transferservice.common.exception;

public class InsufficientBalanceException extends BusinessException {

    public InsufficientBalanceException(String accountNo) {
        super("Insufficient balance: " + accountNo);
    }
}
