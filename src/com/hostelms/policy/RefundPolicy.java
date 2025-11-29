package com.hostelms.policy;

public interface RefundPolicy {
    double calculateRefund(double mealPrice);
    String getPolicyName();
}
