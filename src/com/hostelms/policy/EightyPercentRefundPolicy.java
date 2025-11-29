package com.hostelms.policy;

public class EightyPercentRefundPolicy implements RefundPolicy {
    @Override
    public double calculateRefund(double mealPrice) {
        return mealPrice * 0.80;
    }

    @Override
    public String getPolicyName() {
        return "80% Refund";
    }
}
