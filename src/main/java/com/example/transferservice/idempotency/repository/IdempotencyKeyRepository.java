package com.example.transferservice.idempotency.repository;

import com.example.transferservice.idempotency.domain.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, String> {
}
