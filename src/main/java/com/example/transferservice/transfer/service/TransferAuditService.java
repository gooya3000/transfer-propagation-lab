package com.example.transferservice.transfer.service;

import com.example.transferservice.transfer.domain.TransferAuditLog;
import com.example.transferservice.transfer.repository.TransferAuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferAuditService {

    private final TransferAuditLogRepository transferAuditLogRepository;

    public TransferAuditService(TransferAuditLogRepository transferAuditLogRepository) {
        this.transferAuditLogRepository = transferAuditLogRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordSuccess(Long transferId) {
        transferAuditLogRepository.save(TransferAuditLog.succeeded(transferId));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordFailure(Long transferId, RuntimeException exception) {
        transferAuditLogRepository.save(TransferAuditLog.failed(transferId, exception.getMessage()));
    }
}
