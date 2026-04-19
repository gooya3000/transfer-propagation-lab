package com.example.transferservice.transfer.service;

import com.example.transferservice.idempotency.repository.IdempotencyKeyRepository;
import com.example.transferservice.transfer.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferFailureService {

    private final TransferRepository transferRepository;
    private final IdempotencyKeyRepository idempotencyKeyRepository;

    public TransferFailureService(
            TransferRepository transferRepository,
            IdempotencyKeyRepository idempotencyKeyRepository
    ) {
        this.transferRepository = transferRepository;
        this.idempotencyKeyRepository = idempotencyKeyRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordFailure(Long transferId, String idempotencyKey, String reason) {
        transferRepository.findById(transferId).ifPresent(transfer -> transfer.markFailed(reason));
        idempotencyKeyRepository.findById(idempotencyKey).ifPresent(com.example.transferservice.idempotency.domain.IdempotencyKey::markFailed);
    }
}
