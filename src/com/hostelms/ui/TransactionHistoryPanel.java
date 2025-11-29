package com.hostelms.ui;

import com.hostelms.model.Student;
import com.hostelms.model.Transaction;
import com.hostelms.model.TransactionType;
import com.hostelms.service.TransactionService;
import com.hostelms.util.DataAccessException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TransactionHistoryPanel extends JPanel {
    private Student student;
    private TransactionService transactionService;
    private JTable transactionTable;
    private DefaultTableModel tableModel;

    public TransactionHistoryPanel(Student student) {
        this.student = student;
        this.transactionService = new TransactionService();

        initComponents();
        loadTransactions();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Transaction History", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Date", "Type", "Meal", "Amount", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        add(scrollPane, BorderLayout.CENTER);

        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadTransactions());
        add(refreshButton, BorderLayout.SOUTH);
    }

    private void loadTransactions() {
        try {
            List<Transaction> transactions = transactionService.getTransactionHistory(
                student.getRollNumber());

            // Clear table
            tableModel.setRowCount(0);

            // Add transactions (reverse order - newest first)
            for (int i = transactions.size() - 1; i >= 0; i--) {
                Transaction txn = transactions.get(i);
                String mealType = txn.getMealType() != null ?
                    txn.getMealType().toString() : "-";
                String amount = txn.getType() == TransactionType.CHARGE ?
                    "-" + String.format("%.2f", txn.getAmount()) :
                    "+" + String.format("%.2f", txn.getAmount());

                tableModel.addRow(new Object[]{
                    txn.getDate(),
                    txn.getType(),
                    mealType,
                    amount,
                    txn.getDescription()
                });
            }

        } catch (DataAccessException ex) {
            JOptionPane.showMessageDialog(this,
                "Error loading transactions: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
