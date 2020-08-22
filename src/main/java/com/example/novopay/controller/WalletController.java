package com.example.novopay.controller;

import com.example.novopay.service.WalletService;
import com.example.novopay.utils.PayResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/wallets")
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/money")
    public PayResponse addMoneyToWallet(@PathVariable("userId") String emailId, @PathVariable("amount") Double amount) {
        return walletService.addMoneyToWallet(emailId, amount);
    }
}
