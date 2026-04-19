package com.example.transferservice.idempotency.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency_key")
public class IdempotencyKey {

    @Id
    @Column(name = "idempotency_key", nullable = false, updatable = false)
    private String idempotencyKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IdempotencyKeyStatus status;

    @Column(name = "request_hash", nullable = false, updatable = false)
    private String requestHash;

    @Column(name = "transfer_id", nullable = false)
    private Long transferId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected IdempotencyKey() {
    }

    private IdempotencyKey(String idempotencyKey, String requestHash, Long transferId) {
        this.idempotencyKey = idempotencyKey;
        this.requestHash = requestHash;
        this.transferId = transferId;
        this.status = IdempotencyKeyStatus.IN_PROGRESS;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public static IdempotencyKey inProgress(String idempotencyKey, String requestHash, Long transferId) {
        return new IdempotencyKey(idempotencyKey, requestHash, transferId);
    }

    public String getRequestHash() {
        return requestHash;
    }

    public Long getTransferId() {
        return transferId;
    }

    public IdempotencyKeyStatus getStatus() {
        return status;
    }

    public void markSucceeded() {
        status = IdempotencyKeyStatus.SUCCEEDED;
        updatedAt = LocalDateTime.now();
    }

    public void markFailed() {
        status = IdempotencyKeyStatus.FAILED;
        updatedAt = LocalDateTime.now();
    }
}
