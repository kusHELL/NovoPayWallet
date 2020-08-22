package com.example.novopay.serviceimpl;

import com.example.novopay.model.User;
import com.example.novopay.model.Wallet;
import com.example.novopay.repository.WalletRepository;
import com.example.novopay.service.UserService;
import com.example.novopay.service.WalletService;
import com.example.novopay.utils.PayResponse;
import com.example.novopay.utils.StatusEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final UserService userService;

    @Override
    @Transactional
    public PayResponse addMoneyToWallet(String emailId, Double amount) {
        if(amount <0.1){
            return new PayResponse(StatusEnum.error,"Please enter the correct amount!","Invalid amount");
        }
        Optional<User> optionalUser = userService.findUser(emailId);
        if(optionalUser.isPresent()){
            Optional<Wallet> optionalWallet = walletRepository.findByUserId(emailId);
            if(optionalWallet.isPresent()){
                Wallet tempWallet = optionalWallet.get();
                tempWallet.setBalance(tempWallet.getBalance()+amount);
                walletRepository.save(tempWallet);
                return new PayResponse(StatusEnum.success,"Wallet Balance:"+tempWallet.getBalance(),"Wallet updated");
            }
            else return new PayResponse(StatusEnum.fail,"No wallet available for id","Update failed");
        }
        return new PayResponse(StatusEnum.error,"User Not Available!","Invalid EmailId");
    }

    @Override
    public Optional<Wallet> getWallet(String emailId) {
        return walletRepository.findByUserId(emailId);
    }

    @Override
    @Transactional
    public Wallet updateWallet(Wallet fromWallet) {
        return walletRepository.save(fromWallet);
    }
}
