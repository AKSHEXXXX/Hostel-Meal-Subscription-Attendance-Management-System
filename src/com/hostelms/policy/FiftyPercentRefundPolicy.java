package com.hostelms.policy;

public class FiftyPercentRefundPolicy implements RefundPolicy {
    @Override
    public double calculateRefund(double mealPrice) {
        return mealPrice * 0.50;
    }

    @Override
    public String getPolicyName() {
        return "50% Refund";
    }
}
