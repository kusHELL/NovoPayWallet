package com.example.novopay.service;

import com.example.novopay.dto.PaymentTransferDto;
import com.example.novopay.utils.PayResponse;

public interface TransactionService {
    PayResponse makePayment(PaymentTransferDto paymentTransferDto);

    PayResponse getStatus(Long transactionId);

    PayResponse reverseTransaction(Long transactionId);

    PayResponse getAllTransaction(String emailId);
}
