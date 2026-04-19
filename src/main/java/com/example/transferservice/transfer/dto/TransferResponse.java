package com.example.transferservice.transfer.dto;

import com.example.transferservice.transfer.domain.TransferStatus;

public record TransferResponse(
        Long transferId,
        TransferStatus status,
        boolean reused
) {
}
