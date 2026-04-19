package com.example.transferservice.idempotency.domain;

public enum IdempotencyKeyStatus {
    IN_PROGRESS,
    SUCCEEDED,
    FAILED
}
