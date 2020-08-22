package com.example.novopay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentTransferDto {
    @NonNull
    private String fromUserId;
    @NonNull
    private String toUserId;
    @NonNull
    private Double amount;
}
