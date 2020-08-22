package com.example.novopay.utils;

public enum PaymentStep {
    Completed("Completed"),
    InProgress("In-Progress"),
    Failed("Failed");
    private String step;

    public String getStep() {
        return step;
    }
    PaymentStep(String step) {
        this.step = step;
    }
}
