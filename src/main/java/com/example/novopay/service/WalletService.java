package com.example.novopay.service;

import com.example.novopay.model.Wallet;
import com.example.novopay.utils.PayResponse;

import java.util.Optional;

public interface WalletService {
    PayResponse addMoneyToWallet(String emailId, Double amount);

    Optional<Wallet> getWallet(String emailId);

    Wallet updateWallet(Wallet fromWallet);
}
