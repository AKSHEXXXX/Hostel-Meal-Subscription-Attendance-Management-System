package com.hostelms.model;

import java.time.LocalDate;

public class MealRecord {
    private String rollNumber;
    private LocalDate date;
    private boolean breakfastSelected;
    private boolean lunchSelected;
    private boolean dinnerSelected;
    private String status; // PENDING, COMPLETED, CANCELLED

    public MealRecord(String rollNumber, LocalDate date) {
        this.rollNumber = rollNumber;
        this.date = date;
        this.breakfastSelected = false;
        this.lunchSelected = false;
        this.dinnerSelected = false;
        this.status = "PENDING";
    }

    public boolean isMealSelected(MealType mealType) {
        return switch (mealType) {
            case BREAKFAST -> breakfastSelected;
            case LUNCH -> lunchSelected;
            case DINNER -> dinnerSelected;
        };
    }

    public void selectMeal(MealType mealType) {
        switch (mealType) {
            case BREAKFAST -> breakfastSelected = true;
            case LUNCH -> lunchSelected = true;
            case DINNER -> dinnerSelected = true;
        }
    }

    public void cancelMeal(MealType mealType) {
        switch (mealType) {
            case BREAKFAST -> breakfastSelected = false;
            case LUNCH -> lunchSelected = false;
            case DINNER -> dinnerSelected = false;
        }
    }

    public int getTotalMealsSelected() {
        int count = 0;
        if (breakfastSelected) count++;
        if (lunchSelected) count++;
        if (dinnerSelected) count++;
        return count;
    }

    // Getters and setters
    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isBreakfastSelected() {
        return breakfastSelected;
    }

    public void setBreakfastSelected(boolean breakfastSelected) {
        this.breakfastSelected = breakfastSelected;
    }

    public boolean isLunchSelected() {
        return lunchSelected;
    }

    public void setLunchSelected(boolean lunchSelected) {
        this.lunchSelected = lunchSelected;
    }

    public boolean isDinnerSelected() {
        return dinnerSelected;
    }

    public void setDinnerSelected(boolean dinnerSelected) {
        this.dinnerSelected = dinnerSelected;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
