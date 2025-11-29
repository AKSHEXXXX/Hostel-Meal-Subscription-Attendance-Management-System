package com.hostelms.model;

import java.time.LocalTime;

public class MealConfiguration {
    private double breakfastPrice;
    private double lunchPrice;
    private double dinnerPrice;
    private String refundPolicy; // FULL, EIGHTY_PERCENT, FIFTY_PERCENT
    private LocalTime mealCutoffTime;
    private LocalTime breakfastTime;
    private LocalTime lunchTime;
    private LocalTime dinnerTime;

    public MealConfiguration() {
        // Default constructor
    }

    public double getMealPrice(MealType mealType) {
        return switch (mealType) {
            case BREAKFAST -> breakfastPrice;
            case LUNCH -> lunchPrice;
            case DINNER -> dinnerPrice;
        };
    }

    public void setMealPrice(MealType mealType, double price) {
        switch (mealType) {
            case BREAKFAST -> breakfastPrice = price;
            case LUNCH -> lunchPrice = price;
            case DINNER -> dinnerPrice = price;
        }
    }

    // Getters and setters
    public double getBreakfastPrice() {
        return breakfastPrice;
    }

    public void setBreakfastPrice(double breakfastPrice) {
        this.breakfastPrice = breakfastPrice;
    }

    public double getLunchPrice() {
        return lunchPrice;
    }

    public void setLunchPrice(double lunchPrice) {
        this.lunchPrice = lunchPrice;
    }

    public double getDinnerPrice() {
        return dinnerPrice;
    }

    public void setDinnerPrice(double dinnerPrice) {
        this.dinnerPrice = dinnerPrice;
    }

    public String getRefundPolicy() {
        return refundPolicy;
    }

    public void setRefundPolicy(String refundPolicy) {
        this.refundPolicy = refundPolicy;
    }

    public LocalTime getMealCutoffTime() {
        return mealCutoffTime;
    }

    public void setMealCutoffTime(LocalTime mealCutoffTime) {
        this.mealCutoffTime = mealCutoffTime;
    }

    public LocalTime getBreakfastTime() {
        return breakfastTime;
    }

    public void setBreakfastTime(LocalTime breakfastTime) {
        this.breakfastTime = breakfastTime;
    }

    public LocalTime getLunchTime() {
        return lunchTime;
    }

    public void setLunchTime(LocalTime lunchTime) {
        this.lunchTime = lunchTime;
    }

    public LocalTime getDinnerTime() {
        return dinnerTime;
    }

    public void setDinnerTime(LocalTime dinnerTime) {
        this.dinnerTime = dinnerTime;
    }
}
