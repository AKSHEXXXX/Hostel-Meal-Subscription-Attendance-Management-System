package com.hostelms.dao;

import com.hostelms.model.MealType;
import com.hostelms.model.Transaction;
import com.hostelms.model.TransactionType;
import com.hostelms.util.Constants;
import com.hostelms.util.DataAccessException;
import com.hostelms.util.DateTimeUtil;
import com.hostelms.util.FileCorruptionException;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private static final String FILE_PATH = Constants.TRANSACTIONS_FILE;

    public List<Transaction> loadAllTransactions() throws DataAccessException {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return transactions;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                try {
                    Transaction transaction = parseTransaction(line);
                    transactions.add(transaction);
                } catch (FileCorruptionException e) {
                    System.err.println("Skipping corrupted line: " + line);
                }
            }
        } catch (IOException e) {
            throw new DataAccessException("Error reading transactions file", e);
        }

        return transactions;
    }

    public List<Transaction> findByRollNumber(String rollNumber) throws DataAccessException {
        List<Transaction> allTransactions = loadAllTransactions();
        List<Transaction> result = new ArrayList<>();

        for (Transaction transaction : allTransactions) {
            if (transaction.getRollNumber().equals(rollNumber)) {
                result.add(transaction);
            }
        }

        return result;
    }

    public List<Transaction> findByDateRange(LocalDate start, LocalDate end)
        throws DataAccessException {
        List<Transaction> allTransactions = loadAllTransactions();
        List<Transaction> result = new ArrayList<>();

        for (Transaction transaction : allTransactions) {
            LocalDate txnDate = transaction.getDate();
            if ((txnDate.isEqual(start) || txnDate.isAfter(start)) &&
                (txnDate.isEqual(end) || txnDate.isBefore(end))) {
                result.add(transaction);
            }
        }

        return result;
    }

    public void saveTransaction(Transaction transaction) throws DataAccessException {
        List<Transaction> transactions = loadAllTransactions();
        transactions.add(transaction);
        writeAllTransactions(transactions);
    }

    public String generateNextTransactionId() throws DataAccessException {
        List<Transaction> transactions = loadAllTransactions();

        if (transactions.isEmpty()) {
            return "TXN001";
        }

        // Get last transaction ID
        String lastId = transactions.get(transactions.size() - 1).getTransactionId();

        // Extract number (TXN001 -> 001 -> 1)
        int number = Integer.parseInt(lastId.substring(3));

        // Increment and format (1 -> 2 -> 002 -> TXN002)
        return String.format("TXN%03d", number + 1);
    }

    // Helper methods
    private Transaction parseTransaction(String line) throws FileCorruptionException {
        String[] parts = line.split(",");
        if (parts.length != 8) {
            throw new FileCorruptionException("Invalid transaction format");
        }

        try {
            String transactionId = parts[0].trim();
            String rollNumber = parts[1].trim();
            LocalDate date = DateTimeUtil.parseDate(parts[2].trim());
            TransactionType type = TransactionType.valueOf(parts[3].trim());
            double amount = Double.parseDouble(parts[4].trim());

            MealType mealType = null;
            if (!parts[5].trim().equals("null") && !parts[5].trim().isEmpty()) {
                mealType = MealType.valueOf(parts[5].trim());
            }

            String description = parts[6].trim();

            Transaction transaction = new Transaction(transactionId, rollNumber, date,
                type, amount, mealType, description);

            // Set timestamp if available
            if (parts.length > 7 && !parts[7].trim().isEmpty()) {
                transaction.setTimestamp(DateTimeUtil.parseDateTime(parts[7].trim()));
            }

            return transaction;
        } catch (Exception e) {
            throw new FileCorruptionException("Error parsing transaction: " + e.getMessage());
        }
    }

    private String transactionToString(Transaction transaction) {
        String mealTypeStr = transaction.getMealType() != null ?
            transaction.getMealType().toString() : "null";

        return String.format("%s,%s,%s,%s,%.2f,%s,%s,%s",
            transaction.getTransactionId(),
            transaction.getRollNumber(),
            DateTimeUtil.formatDate(transaction.getDate()),
            transaction.getType(),
            transaction.getAmount(),
            mealTypeStr,
            transaction.getDescription(),
            DateTimeUtil.formatDateTime(transaction.getTimestamp()));
    }

    private void writeAllTransactions(List<Transaction> transactions) throws DataAccessException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Transaction transaction : transactions) {
                writer.write(transactionToString(transaction));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DataAccessException("Error writing transactions file", e);
        }
    }
}
