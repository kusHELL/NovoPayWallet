package com.example.novopay.repository;

import com.example.novopay.model.Transaction;
import com.example.novopay.utils.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByFromUserIdAndToUserIdAndTimeStampAndPaymentType(String fromUserId, String toUserId, Long timeStamp, PaymentType paymentType);

    List<Transaction> findAllByFromUserIdOrToUserId(String emailId, String emailId1);
}
