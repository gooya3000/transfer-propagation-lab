package com.example.transferservice.transfer.repository;

import com.example.transferservice.transfer.domain.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {
}
