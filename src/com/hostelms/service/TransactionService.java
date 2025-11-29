package com.hostelms.service;

import com.hostelms.dao.StudentDAO;
import com.hostelms.dao.TransactionDAO;
import com.hostelms.model.*;
import com.hostelms.observer.NotificationCenter;
import com.hostelms.util.DataAccessException;
import com.hostelms.util.InsufficientBalanceException;

import java.time.LocalDate;
import java.util.List;

public class TransactionService {
    private TransactionDAO transactionDAO;
    private StudentDAO studentDAO;

    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
        this.studentDAO = new StudentDAO();
    }

    public void chargeMeal(String rollNumber, LocalDate date, MealType mealType, double amount)
        throws DataAccessException, InsufficientBalanceException {

        Student student = studentDAO.findByRollNumber(rollNumber);
        if (student == null) {
            throw new DataAccessException("Student not found: " + rollNumber);
        }

        // Deduct balance
        student.deductBalance(amount);
        studentDAO.updateStudent(student);

        // Create transaction
        String txnId = transactionDAO.generateNextTransactionId();
        Transaction transaction = new Transaction(txnId, rollNumber, date,
            TransactionType.CHARGE, amount, mealType, "Charge for " + mealType);
        transactionDAO.saveTransaction(transaction);

        // Notify student
        Notification notification = new Notification(
            String.format("Charged Rs. %.2f for %s. Balance: Rs. %.2f",
                amount, mealType, student.getBalance()), "INFO");
        NotificationCenter.getInstance().notifyStudent(rollNumber, notification);
    }

    public void refundMeal(String rollNumber, LocalDate date, MealType mealType,
                          double amount, String policyName) throws DataAccessException {

        Student student = studentDAO.findByRollNumber(rollNumber);
        if (student == null) {
            throw new DataAccessException("Student not found: " + rollNumber);
        }

        // Add balance
        student.addBalance(amount);
        studentDAO.updateStudent(student);

        // Create transaction
        String txnId = transactionDAO.generateNextTransactionId();
        Transaction transaction = new Transaction(txnId, rollNumber, date,
            TransactionType.REFUND, amount, mealType,
            policyName + " for missed " + mealType);
        transactionDAO.saveTransaction(transaction);

        // Notify student
        Notification notification = new Notification(
            String.format("Refunded Rs. %.2f for missed %s. Balance: Rs. %.2f",
                amount, mealType, student.getBalance()), "SUCCESS");
        NotificationCenter.getInstance().notifyStudent(rollNumber, notification);
    }

    public void rechargeBalance(String rollNumber, double amount)
        throws DataAccessException {

        Student student = studentDAO.findByRollNumber(rollNumber);
        if (student == null) {
            throw new DataAccessException("Student not found: " + rollNumber);
        }

        student.addBalance(amount);
        studentDAO.updateStudent(student);

        // Create transaction
        String txnId = transactionDAO.generateNextTransactionId();
        Transaction transaction = new Transaction(txnId, rollNumber, LocalDate.now(),
            TransactionType.RECHARGE, amount, null, "Balance recharge");
        transactionDAO.saveTransaction(transaction);

        // Notify student
        Notification notification = new Notification(
            String.format("Balance recharged by Rs. %.2f. New balance: Rs. %.2f",
                amount, student.getBalance()), "SUCCESS");
        NotificationCenter.getInstance().notifyStudent(rollNumber, notification);
    }

    public List<Transaction> getTransactionHistory(String rollNumber)
        throws DataAccessException {
        return transactionDAO.findByRollNumber(rollNumber);
    }
}
