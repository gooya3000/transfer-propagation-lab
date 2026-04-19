package com.example.transferservice.transfer.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfer_audit_log")
public class TransferAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transfer_id")
    private Long transferId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private AuditEventType eventType;

    @Column(nullable = false)
    private String message;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected TransferAuditLog() {
    }

    private TransferAuditLog(Long transferId, AuditEventType eventType, String message) {
        this.transferId = transferId;
        this.eventType = eventType;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    public static TransferAuditLog succeeded(Long transferId) {
        return new TransferAuditLog(transferId, AuditEventType.TRANSFER_SUCCEEDED, "transfer succeeded");
    }

    public static TransferAuditLog failed(Long transferId, String message) {
        return new TransferAuditLog(transferId, AuditEventType.TRANSFER_FAILED, message);
    }
}
