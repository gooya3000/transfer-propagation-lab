package com.example.transferservice.transfer.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfer")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_account_no", nullable = false)
    private String senderAccountNo;

    @Column(name = "receiver_account_no", nullable = false)
    private String receiverAccountNo;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferStatus status;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Transfer() {
    }

    private Transfer(String senderAccountNo, String receiverAccountNo, BigDecimal amount, String idempotencyKey) {
        this.senderAccountNo = senderAccountNo;
        this.receiverAccountNo = receiverAccountNo;
        this.amount = amount;
        this.idempotencyKey = idempotencyKey;
        this.status = TransferStatus.IN_PROGRESS;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public static Transfer inProgress(String senderAccountNo, String receiverAccountNo, BigDecimal amount, String idempotencyKey) {
        return new Transfer(senderAccountNo, receiverAccountNo, amount, idempotencyKey);
    }

    public Long getId() {
        return id;
    }

    public String getSenderAccountNo() {
        return senderAccountNo;
    }

    public String getReceiverAccountNo() {
        return receiverAccountNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public boolean isCompleted() {
        return status == TransferStatus.SUCCEEDED || status == TransferStatus.FAILED;
    }

    public void markSucceeded() {
        status = TransferStatus.SUCCEEDED;
        failureReason = null;
        updatedAt = LocalDateTime.now();
    }

    public void markFailed(String reason) {
        status = TransferStatus.FAILED;
        failureReason = reason;
        updatedAt = LocalDateTime.now();
    }
}
