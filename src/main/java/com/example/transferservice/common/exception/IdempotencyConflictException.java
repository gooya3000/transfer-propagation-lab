package com.example.transferservice.common.exception;

public class IdempotencyConflictException extends BusinessException {

    public IdempotencyConflictException(String idempotencyKey) {
        super("Same idempotency key was used with a different payload: " + idempotencyKey);
    }
}
