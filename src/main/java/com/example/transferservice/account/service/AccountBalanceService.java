package com.example.transferservice.account.service;

import com.example.transferservice.account.domain.Account;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountBalanceService {

    @Transactional(propagation = Propagation.MANDATORY)
    public void debit(Account account, BigDecimal amount) {
        account.debit(amount);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void credit(Account account, BigDecimal amount) {
        account.credit(amount);
    }
}
