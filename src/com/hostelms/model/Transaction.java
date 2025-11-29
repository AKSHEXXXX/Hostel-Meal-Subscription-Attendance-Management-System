package com.hostelms.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Transaction {
    private String transactionId;
    private String rollNumber;
    private LocalDate date;
    private TransactionType type;
    private double amount;
    private MealType mealType; // null for recharge
    private String description;
    private LocalDateTime timestamp;

    public Transaction(String transactionId, String rollNumber, LocalDate date,
                      TransactionType type, double amount, MealType mealType,
                      String description) {
        this.transactionId = transactionId;
        this.rollNumber = rollNumber;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.mealType = mealType;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("Transaction{id='%s', roll='%s', type=%s, amount=%.2f, meal=%s}",
            transactionId, rollNumber, type, amount, mealType);
    }

    // Getters and setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

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

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
