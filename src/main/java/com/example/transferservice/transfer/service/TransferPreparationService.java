package com.example.transferservice.transfer.service;

import com.example.transferservice.common.exception.IdempotencyConflictException;
import com.example.transferservice.idempotency.domain.IdempotencyKey;
import com.example.transferservice.idempotency.repository.IdempotencyKeyRepository;
import com.example.transferservice.transfer.domain.Transfer;
import com.example.transferservice.transfer.dto.PreparedTransfer;
import com.example.transferservice.transfer.dto.TransferCommand;
import com.example.transferservice.transfer.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferPreparationService {

    private final TransferRepository transferRepository;
    private final IdempotencyKeyRepository idempotencyKeyRepository;

    public TransferPreparationService(
            TransferRepository transferRepository,
            IdempotencyKeyRepository idempotencyKeyRepository
    ) {
        this.transferRepository = transferRepository;
        this.idempotencyKeyRepository = idempotencyKeyRepository;
    }

    @Transactional
    public PreparedTransfer prepare(TransferCommand command) {
        String requestHash = command.payloadHash();

        return idempotencyKeyRepository.findById(command.idempotencyKey())
                .map(existing -> reuseExisting(command.idempotencyKey(), requestHash, existing))
                .orElseGet(() -> createNewTransfer(command, requestHash));
    }

    private PreparedTransfer reuseExisting(String idempotencyKey, String requestHash, IdempotencyKey existing) {
        if (!existing.getRequestHash().equals(requestHash)) {
            throw new IdempotencyConflictException(idempotencyKey);
        }

        Transfer transfer = transferRepository.findById(existing.getTransferId())
                .orElseThrow(() -> new IllegalStateException("idempotency key points to missing transfer"));
        return new PreparedTransfer(transfer.getId(), transfer.getStatus(), true);
    }

    private PreparedTransfer createNewTransfer(TransferCommand command, String requestHash) {
        Transfer transfer = transferRepository.save(Transfer.inProgress(
                command.senderAccountNo(),
                command.receiverAccountNo(),
                command.amount(),
                command.idempotencyKey()
        ));
        idempotencyKeyRepository.save(IdempotencyKey.inProgress(
                command.idempotencyKey(),
                requestHash,
                transfer.getId()
        ));
        return new PreparedTransfer(transfer.getId(), transfer.getStatus(), false);
    }
}
