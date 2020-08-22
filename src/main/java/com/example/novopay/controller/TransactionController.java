package com.example.novopay.controller;

import com.example.novopay.dto.PaymentTransferDto;
import com.example.novopay.service.TransactionService;
import com.example.novopay.utils.PayResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/payment")
    public PayResponse makePayment(@RequestBody PaymentTransferDto paymentTransferDto) {
        return transactionService.makePayment(paymentTransferDto);
    }

    @GetMapping("/status")
    public PayResponse getStatus(@RequestParam("transactionId") Long transactionId) {
        return transactionService.getStatus(transactionId);
    }

    @PostMapping("/reversal")
    public PayResponse reverseTransaction(@RequestBody Long transactionId) {
        return transactionService.reverseTransaction(transactionId);
    }

    @GetMapping("/all")
    public PayResponse getAllTransaction(@RequestParam("userId") String emailId) {
        return transactionService.getAllTransaction(emailId);
    }

}
