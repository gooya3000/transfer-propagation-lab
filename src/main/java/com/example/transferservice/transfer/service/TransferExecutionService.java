package com.example.transferservice.transfer.service;

import com.example.transferservice.account.domain.Account;
import com.example.transferservice.account.repository.AccountRepository;
import com.example.transferservice.account.service.AccountBalanceService;
import com.example.transferservice.common.exception.NotFoundException;
import com.example.transferservice.idempotency.domain.IdempotencyKey;
import com.example.transferservice.idempotency.repository.IdempotencyKeyRepository;
import com.example.transferservice.transfer.domain.Transfer;
import com.example.transferservice.transfer.dto.PreparedTransfer;
import com.example.transferservice.transfer.dto.TransferCommand;
import com.example.transferservice.transfer.dto.TransferResponse;
import com.example.transferservice.transfer.repository.TransferRepository;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferExecutionService {

    private final TransferRepository transferRepository;
    private final IdempotencyKeyRepository idempotencyKeyRepository;
    private final AccountRepository accountRepository;
    private final AccountBalanceService accountBalanceService;
    private final AccountTransactionHistoryService transactionHistoryService;

    public TransferExecutionService(
            TransferRepository transferRepository,
            IdempotencyKeyRepository idempotencyKeyRepository,
            AccountRepository accountRepository,
            AccountBalanceService accountBalanceService,
            AccountTransactionHistoryService transactionHistoryService
    ) {
        this.transferRepository = transferRepository;
        this.idempotencyKeyRepository = idempotencyKeyRepository;
        this.accountRepository = accountRepository;
        this.accountBalanceService = accountBalanceService;
        this.transactionHistoryService = transactionHistoryService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public TransferResponse execute(PreparedTransfer prepared, TransferCommand command) {
        Transfer transfer = transferRepository.findById(prepared.transferId())
                .orElseThrow(() -> new NotFoundException("transfer not found: " + prepared.transferId()));

        if (transfer.isCompleted()) {
            return new TransferResponse(transfer.getId(), transfer.getStatus(), true);
        }

        Map<String, Account> accounts = lockAccountsInFixedOrder(command);
        Account sender = accounts.get(command.senderAccountNo());
        Account receiver = accounts.get(command.receiverAccountNo());

        accountBalanceService.debit(sender, command.amount());
        accountBalanceService.credit(receiver, command.amount());
        transactionHistoryService.recordDebit(sender.getAccountNo(), transfer.getId(), command.amount());
        transactionHistoryService.recordCredit(receiver.getAccountNo(), transfer.getId(), command.amount());

        transfer.markSucceeded();
        IdempotencyKey idempotencyKey = idempotencyKeyRepository.findById(command.idempotencyKey())
                .orElseThrow(() -> new NotFoundException("idempotency key not found: " + command.idempotencyKey()));
        idempotencyKey.markSucceeded();

        return new TransferResponse(transfer.getId(), transfer.getStatus(), prepared.reused());
    }

    private Map<String, Account> lockAccountsInFixedOrder(TransferCommand command) {
        List<String> accountNos = Stream.of(command.senderAccountNo(), command.receiverAccountNo())
                .sorted()
                .toList();
        List<Account> accounts = accountRepository.findAllByAccountNoInForUpdate(accountNos);
        if (accounts.size() != 2) {
            throw new NotFoundException("sender or receiver account not found");
        }
        return accounts.stream().collect(Collectors.toMap(Account::getAccountNo, Function.identity()));
    }
}
