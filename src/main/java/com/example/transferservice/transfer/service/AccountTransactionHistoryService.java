package com.example.transferservice.transfer.service;

import com.example.transferservice.transfer.domain.AccountTransaction;
import com.example.transferservice.transfer.repository.AccountTransactionRepository;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountTransactionHistoryService {

    private final AccountTransactionRepository accountTransactionRepository;

    public AccountTransactionHistoryService(AccountTransactionRepository accountTransactionRepository) {
        this.accountTransactionRepository = accountTransactionRepository;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void recordDebit(String accountNo, Long transferId, BigDecimal amount) {
        accountTransactionRepository.save(AccountTransaction.debit(accountNo, transferId, amount));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void recordCredit(String accountNo, Long transferId, BigDecimal amount) {
        accountTransactionRepository.save(AccountTransaction.credit(accountNo, transferId, amount));
    }
}
