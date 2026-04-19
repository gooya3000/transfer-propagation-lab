package com.example.transferservice.account.repository;

import com.example.transferservice.account.domain.Account;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.accountNo in :accountNos order by a.accountNo")
    List<Account> findAllByAccountNoInForUpdate(@Param("accountNos") Collection<String> accountNos);
}
