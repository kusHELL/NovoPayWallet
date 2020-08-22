package com.example.novopay.serviceimpl;

import com.example.novopay.constants.PaymentConstants;
import com.example.novopay.dto.PaymentTransferDto;
import com.example.novopay.model.Transaction;
import com.example.novopay.model.User;
import com.example.novopay.model.Wallet;
import com.example.novopay.repository.TransactionRepository;
import com.example.novopay.service.TransactionService;
import com.example.novopay.service.UserService;
import com.example.novopay.service.WalletService;
import com.example.novopay.utils.PayResponse;
import com.example.novopay.utils.PaymentStep;
import com.example.novopay.utils.PaymentType;
import com.example.novopay.utils.StatusEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final UserService userService;
    private final WalletService walletService;
    private final TransactionRepository transactionRepository;

    @Transactional
    @Override
    public PayResponse makePayment(PaymentTransferDto paymentTransferDto) {
        Long timeStamp = getTimeStamp();
        Optional<User> fromUserOptional = userService.findUser(paymentTransferDto.getFromUserId());
        Optional<User> toUserOptional = userService.findUser(paymentTransferDto.getToUserId());
        if (!fromUserOptional.isPresent() || !toUserOptional.isPresent()) {
            return new PayResponse(StatusEnum.error, "Invalid id(s).", "Invalid id(s).");
        }
        User fromUser = fromUserOptional.get();
        User toUser = toUserOptional.get();
        Optional<Wallet> fromWalletOptional = walletService.getWallet(fromUser.getEmailId());
        Optional<Wallet> toWalletOptional = walletService.getWallet(toUser.getEmailId());
        if (!fromWalletOptional.isPresent() || !toWalletOptional.isPresent()) {
            return new PayResponse(StatusEnum.error, "Invalid id(s).", "Invalid id(s).");
        }
        Wallet fromWallet = fromWalletOptional.get();
        Wallet toWallet = toWalletOptional.get();
        Double commissionForTransaction = (paymentTransferDto.getAmount() * PaymentConstants.COMMISSION) / 100;
        Double chargeForTransaction = (paymentTransferDto.getAmount() * PaymentConstants.CHARGE) / 100;
        if (fromWallet.getBalance() < (paymentTransferDto.getAmount() + commissionForTransaction + chargeForTransaction)) {
            return new PayResponse(StatusEnum.fail, "Insufficient balance.", "Insufficient balance.");
        }
        try {
            fromWallet.setBalance(fromWallet.getBalance()
                    - (paymentTransferDto.getAmount() + commissionForTransaction + chargeForTransaction));
            walletService.updateWallet(fromWallet);
            toWallet.setBalance(toWallet.getBalance() +
                    (paymentTransferDto.getAmount() + commissionForTransaction + chargeForTransaction));
            walletService.updateWallet(toWallet);
            Transaction debitTransaction = new Transaction(fromWallet.getId(),
                    fromUser.getEmailId(),
                    toWallet.getId(),
                    toUser.getEmailId(),
                    false,
                    null,
                    timeStamp,
                    paymentTransferDto.getAmount(),
                    PaymentStep.Completed,
                    PaymentType.Debit);
            Transaction creditTransaction = new Transaction(fromWallet.getId(),
                    fromUser.getEmailId(),
                    toWallet.getId(),
                    toUser.getEmailId(),
                    false,
                    null,
                    timeStamp,
                    paymentTransferDto.getAmount(),
                    PaymentStep.Completed,
                    PaymentType.Credit);
            debitTransaction = transactionRepository.save(debitTransaction);
            creditTransaction = transactionRepository.save(creditTransaction);
            List<Transaction> transactionList = new ArrayList<>();
            transactionList.add(debitTransaction);
            transactionList.add(creditTransaction);
            return new PayResponse(StatusEnum.success, "Transaction Completed:" + transactionList, "Transaction completed");
        } catch (Exception e) {
            return new PayResponse(StatusEnum.error, "Could not complete transaction!", "Something went wrong");
        }
    }

    @Override
    public PayResponse getStatus(Long transactionId) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
        if (transactionOptional.isPresent()) {
            return new PayResponse(StatusEnum.success, transactionOptional.get().getPaymentStep().getStep(), "Success");
        }
        return new PayResponse(StatusEnum.error, "Invalid Id.", "Failure.");
    }

    @Override
    public PayResponse reverseTransaction(Long transactionId) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
        if (!transactionOptional.isPresent()) {
            return new PayResponse(StatusEnum.error, "Invalid Id.", "Failure.");
        }
        Transaction transaction = transactionOptional.get();
        Optional<Transaction> otherTransaction = transactionRepository
                .findByFromUserIdAndToUserIdAndTimeStampAndPaymentType(transaction.getFromUserId()
                        , transaction.getToUserId()
                        , transaction.getTimeStamp()
                        , transaction.getPaymentType() == PaymentType.Credit ? PaymentType.Debit : PaymentType.Credit);
        if (!otherTransaction.isPresent()) {
            return new PayResponse(StatusEnum.error, "Invalid Id.", "Failure.");
        }
        PaymentTransferDto paymentTransferDto = new PaymentTransferDto(transaction.getToUserId(), transaction.getFromUserId(), transaction.getAmount());
        PayResponse payResponse = makePayment(paymentTransferDto);
        if (payResponse.getStatus() != StatusEnum.success) {
            return payResponse;
        }
        List<Transaction> transactionList = (List<Transaction>) payResponse.getData();
        transaction.setIsReversed(true);
        transaction.setReversedTransactionId(transactionList.get(0).getId());
        Transaction temp = otherTransaction.get();
        temp.setIsReversed(true);
        temp.setReversedTransactionId(transactionList.get(0).getId());
        return new PayResponse(StatusEnum.success, payResponse.getData(), "Transaction reversed.");
    }

    @Override
    public PayResponse getAllTransaction(String emailId) {
        List<Transaction> transactionList = transactionRepository.findAllByFromUserIdOrToUserId(emailId, emailId);
        if (transactionList.isEmpty()) {
            return new PayResponse(StatusEnum.error, "Invalid Id.", "Failure.");
        }
        return new PayResponse(StatusEnum.success, transactionList, "Success");
    }

    private Long getTimeStamp() {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
        String currentTime = simpleDateFormat.format(today);
        try {
            Date date = simpleDateFormat.parse(currentTime);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1L;
        }
    }
}
