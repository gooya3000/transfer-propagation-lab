package com.example.transferservice.transfer.repository;

import com.example.transferservice.transfer.domain.TransferAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferAuditLogRepository extends JpaRepository<TransferAuditLog, Long> {
}
