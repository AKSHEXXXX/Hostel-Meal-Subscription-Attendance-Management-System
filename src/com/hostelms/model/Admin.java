package com.hostelms.model;

public class Admin extends User {
    private double balance;

    public Admin(String rollNumber, String name, String password) {
        super(rollNumber, name, password);
        this.balance = 0.0;
    }

    public Admin(String rollNumber, String name, String password, double balance) {
        super(rollNumber, name, password);
        this.balance = balance;
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addBalance(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    public void deductBalance(double amount) {
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
        }
    }
}
