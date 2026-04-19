package com.example.transferservice.transfer.dto;

import com.example.transferservice.transfer.domain.TransferStatus;

public record PreparedTransfer(
        Long transferId,
        TransferStatus status,
        boolean reused
) {
}
