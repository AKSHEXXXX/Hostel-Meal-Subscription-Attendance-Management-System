package com.hostelms.ui;

import com.hostelms.model.Student;
import com.hostelms.service.AuthenticationService;
import java.awt.*;
import javax.swing.*;

public class StudentDashboard extends JFrame {
    private final Student student;
    private JLabel balanceLabel;
    private JTabbedPane tabbedPane;

    private MealSelectionPanel mealSelectionPanel;
    private TransactionHistoryPanel transactionHistoryPanel;
    private NotificationPanel notificationPanel;

    public StudentDashboard(Student student) {
        this.student = student;
        initComponents();
    }

    private void initComponents() {
        setTitle("Student Dashboard - " + student.getName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel - Balance and logout
        JPanel topPanel = new JPanel(new BorderLayout());
        balanceLabel = new JLabel("Balance: AED " +
            String.format("%.2f", student.getBalance()));
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(balanceLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout());
        topPanel.add(logoutButton, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Tabbed pane
        tabbedPane = new JTabbedPane();

        mealSelectionPanel = new MealSelectionPanel(student, this);
        transactionHistoryPanel = new TransactionHistoryPanel(student);
        notificationPanel = new NotificationPanel(student);

        tabbedPane.addTab("Meal Selection", mealSelectionPanel);
        tabbedPane.addTab("Transaction History", transactionHistoryPanel);
        tabbedPane.addTab("Notifications", notificationPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    public void updateBalance() {
        balanceLabel.setText("Balance: AED " +
            String.format("%.2f", student.getBalance()));
    }

    private void handleLogout() {
        new AuthenticationService().logout(student);
        dispose();
        new LoginFrame().setVisible(true);
    }
}
