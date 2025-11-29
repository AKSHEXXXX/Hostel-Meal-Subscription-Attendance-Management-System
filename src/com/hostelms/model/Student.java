package com.hostelms.model;

import com.hostelms.observer.Observer;
import com.hostelms.util.InsufficientBalanceException;

import java.util.ArrayList;
import java.util.List;

public class Student extends User implements Observer {
    private double balance;
    private List<Notification> notifications;

    public Student(String rollNumber, String name, String password, double balance) {
        super(rollNumber, name, password);
        this.balance = balance;
        this.notifications = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return "STUDENT";
    }

    @Override
    public void update(Notification notification) {
        notifications.add(notification);
    }

    public void addBalance(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    public void deductBalance(double amount) throws InsufficientBalanceException {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot deduct negative amount");
        }

        if (this.balance < amount) {
            throw new InsufficientBalanceException(
                String.format("Insufficient balance. Required: Rs. %.2f, Available: Rs. %.2f",
                    amount, this.balance));
        }

        this.balance -= amount;
    }

    // Getters and setters
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
