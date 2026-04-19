package com.example.transferservice.transfer.service;

import com.example.transferservice.transfer.dto.PreparedTransfer;
import com.example.transferservice.transfer.dto.TransferCommand;
import com.example.transferservice.transfer.dto.TransferResponse;
import org.springframework.stereotype.Service;

@Service
public class TransferFacadeService {

    private final TransferPreparationService preparationService;
    private final TransferExecutionService executionService;
    private final TransferFailureService failureService;
    private final TransferAuditService auditService;
    private final TransferNotificationService notificationService;

    public TransferFacadeService(
            TransferPreparationService preparationService,
            TransferExecutionService executionService,
            TransferFailureService failureService,
            TransferAuditService auditService,
            TransferNotificationService notificationService
    ) {
        this.preparationService = preparationService;
        this.executionService = executionService;
        this.failureService = failureService;
        this.auditService = auditService;
        this.notificationService = notificationService;
    }

    public TransferResponse transfer(TransferCommand command) {
        PreparedTransfer prepared = preparationService.prepare(command);

        try {
            TransferResponse response = executionService.execute(prepared, command);
            auditService.recordSuccess(response.transferId());
            notificationService.notifyTransferResult(response);
            return response;
        } catch (RuntimeException e) {
            // T2 rollback 이후에도 실패 상태와 감사 로그는 반드시 남기기 위해 별도 bean의 REQUIRES_NEW를 호출한다.
            failureService.recordFailure(prepared.transferId(), command.idempotencyKey(), e.getMessage());
            auditService.recordFailure(prepared.transferId(), e);
            throw e;
        }
    }
}
