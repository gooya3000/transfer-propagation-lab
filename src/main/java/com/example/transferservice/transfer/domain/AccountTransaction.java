package com.example.transferservice.transfer.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "account_transaction")
public class AccountTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_no", nullable = false)
    private String accountNo;

    @Column(name = "transfer_id", nullable = false)
    private Long transferId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountTransactionType type;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected AccountTransaction() {
    }

    private AccountTransaction(String accountNo, Long transferId, BigDecimal amount, AccountTransactionType type) {
        this.accountNo = accountNo;
        this.transferId = transferId;
        this.amount = amount;
        this.type = type;
        this.createdAt = LocalDateTime.now();
    }

    public static AccountTransaction debit(String accountNo, Long transferId, BigDecimal amount) {
        return new AccountTransaction(accountNo, transferId, amount, AccountTransactionType.DEBIT);
    }

    public static AccountTransaction credit(String accountNo, Long transferId, BigDecimal amount) {
        return new AccountTransaction(accountNo, transferId, amount, AccountTransactionType.CREDIT);
    }
}
