package com.example.novopay.model;

import com.example.novopay.utils.PaymentStep;
import com.example.novopay.utils.PaymentType;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long fromWalletId;

    private String fromUserId;

    private Long toWalletId;

    private String toUserId;

    private Boolean isReversed;

    private Long reversedTransactionId;

    private Long timeStamp;

    private Double amount;

    private PaymentStep paymentStep;

    private PaymentType paymentType;

    public Transaction(Long fromWalletId
            , String fromUserId
            , Long toWalletId
            , String toUserId
            , Boolean isReversed
            , Long reversedTransactionId
            , Long timeStamp
            , Double amount
            , PaymentStep paymentStep
            , PaymentType paymentType) {
        this.fromWalletId = fromWalletId;
        this.fromUserId = fromUserId;
        this.toWalletId = toWalletId;
        this.toUserId = toUserId;
        this.isReversed = isReversed;
        this.reversedTransactionId = reversedTransactionId;
        this.timeStamp = timeStamp;
        this.amount = amount;
        this.paymentStep = paymentStep;
        this.paymentType = paymentType;
    }
}
