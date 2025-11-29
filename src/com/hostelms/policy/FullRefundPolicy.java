package com.hostelms.policy;

public class FullRefundPolicy implements RefundPolicy {
    @Override
    public double calculateRefund(double mealPrice) {
        return mealPrice;
    }

    @Override
    public String getPolicyName() {
        return "Full Refund (100%)";
    }
}
