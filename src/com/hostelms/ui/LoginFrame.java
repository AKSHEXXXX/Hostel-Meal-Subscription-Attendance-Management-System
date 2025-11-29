package com.hostelms.ui;

import com.hostelms.model.Admin;
import com.hostelms.model.Student;
import com.hostelms.model.User;
import com.hostelms.service.AuthenticationService;
import com.hostelms.util.DataAccessException;
import com.hostelms.util.InvalidCredentialsException;
import java.awt.*;
import javax.swing.*;

public class LoginFrame extends JFrame {
    // Components
    private JTextField rollNumberField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    private AuthenticationService authService;
    private String windowTitle;

    public LoginFrame() {
        this("Hostel Meal Management - Login");
    }

    public LoginFrame(String title) {
        this.windowTitle = title;
        authService = new AuthenticationService();
        initComponents();
    }

    private void initComponents() {
        setTitle(windowTitle);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Hostel Meal Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form panel with GridLayout
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.add(new JLabel("Roll Number:"));
        rollNumberField = new JTextField();
        formPanel.add(rollNumberField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        formPanel.add(new JLabel("")); // Spacer
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        formPanel.add(loginButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Status label
        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setForeground(Color.RED);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        add(mainPanel);

        // Enter key listener
        passwordField.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String rollNumber = rollNumberField.getText().trim();
        String password = new String(passwordField.getPassword());

        try {
            User user = authService.login(rollNumber, password);

            // Don't close login frame - keep it open
            // Just hide it
            setVisible(false);

            // Open appropriate dashboard
            if (user instanceof Student student) {
                StudentDashboard dashboard = new StudentDashboard(student);
                dashboard.setVisible(true);
                // Position dashboard based on which login window this is
                positionDashboard(dashboard);
            } else if (user instanceof Admin admin) {
                AdminDashboard dashboard = new AdminDashboard(admin);
                dashboard.setVisible(true);
                // Position dashboard based on which login window this is
                positionDashboard(dashboard);
            }

        } catch (InvalidCredentialsException ex) {
            statusLabel.setText(ex.getMessage());
        } catch (DataAccessException ex) {
            statusLabel.setText("Error accessing data: " + ex.getMessage());
        }
    }

    private void positionDashboard(JFrame dashboard) {
        // Position dashboard near its login window
        java.awt.Point loginLocation = getLocation();
        dashboard.setLocation(loginLocation.x, loginLocation.y);
    }
}
