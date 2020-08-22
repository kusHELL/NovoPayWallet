package com.example.novopay.utils;

public enum PaymentType {
    Credit("Credit"),
    Debit("Debit");

    private String type;

    PaymentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }


}
