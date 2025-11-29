package com.hostelms.ui;

import com.hostelms.model.Admin;
import java.awt.*;
import javax.swing.*;

public class AdminDashboard extends JFrame {
    private final Admin admin;
    private JTabbedPane tabbedPane;

    private ConfigurationPanel configurationPanel;
    private AttendancePanel attendancePanel;
    private ReportPanel reportPanel;

    public AdminDashboard(Admin admin) {
        this.admin = admin;
        initComponents();
    }

    private void initComponents() {
        setTitle("Admin Dashboard - " + admin.getName());
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel - Title and logout
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Administrator Panel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(titleLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout());
        topPanel.add(logoutButton, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Tabbed pane
        tabbedPane = new JTabbedPane();

        configurationPanel = new ConfigurationPanel();
        attendancePanel = new AttendancePanel();
        reportPanel = new ReportPanel();

        tabbedPane.addTab("Configuration", configurationPanel);
        tabbedPane.addTab("Mark Attendance", attendancePanel);
        tabbedPane.addTab("Reports", reportPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void handleLogout() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}
