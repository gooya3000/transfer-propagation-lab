package com.example.transferservice.transfer;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("Integration test draft. Fill fixtures after API and seed helpers are implemented.")
@SpringBootTest
class TransferPropagationIntegrationTest {

    @Test
    void normalTransferCommitsBalanceHistoryStatusAndAuditLog() {
        // given: sender/receiver accounts and a new idempotency key
        // when: TransferFacadeService.transfer is called
        // then: balances are changed, two account transactions exist,
        //       transfer/idempotency are SUCCEEDED, audit log is persisted
    }

    @Test
    void insufficientBalanceRollsBackBalanceAndHistoryButRecordsFailureInRequiresNew() {
        // given: sender account balance is lower than transfer amount
        // when: TransferFacadeService.transfer is called
        // then: balance and account_transaction are rolled back,
        //       transfer/idempotency are FAILED, failure audit log remains
    }

    @Test
    void sameIdempotencyKeyWithSamePayloadReusesExistingTransfer() {
        // given: an already processed idempotency key
        // when: same key and same payload are requested again
        // then: no duplicate transfer is created and previous result is returned
    }

    @Test
    void sameIdempotencyKeyWithDifferentPayloadThrowsConflict() {
        // given: an existing idempotency key with payload hash A
        // when: same key is requested with payload hash B
        // then: IdempotencyConflictException is thrown
    }

    @Test
    void mandatoryMethodsRequireExecutionTransaction() {
        // given: AccountBalanceService or AccountTransactionHistoryService
        // when: MANDATORY method is called without TransferExecutionService transaction
        // then: IllegalTransactionStateException is thrown
    }

    @Test
    void t1SuccessAndT2FailureLeavesFailedState() {
        // given: preparation succeeds and execution is forced to fail
        // when: TransferFacadeService.transfer is called
        // then: T2 changes are rolled back but FAILED state is committed by REQUIRES_NEW
    }
}
