package com.example.transferservice.transfer.service;

import com.example.transferservice.transfer.dto.TransferResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferNotificationService {

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void notifyTransferResult(TransferResponse response) {
        // 알림은 핵심 이체 트랜잭션과 분리한다. 실패해도 잔액/거래내역 정합성에 영향을 주지 않아야 한다.
    }
}
