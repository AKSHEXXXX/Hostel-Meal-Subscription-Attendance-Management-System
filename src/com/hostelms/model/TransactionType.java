package com.hostelms.model;

public enum TransactionType {
    CHARGE("Charge"),
    REFUND("Refund"),
    RECHARGE("Recharge");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
