package com.hostelms;

import com.hostelms.dao.ConfigurationDAO;
import com.hostelms.dao.StudentDAO;
import com.hostelms.ui.LoginFrame;
import com.hostelms.util.Constants;
import java.io.File;
import javax.swing.SwingUtilities;

public class Main {
    @SuppressWarnings({"CallToPrintStackTrace", "UseSpecificCatch"})
    public static void main(String[] args) {
        // Initialize data directory
        initializeDataDirectory();

        // Initialize configuration if not exists
        initializeConfiguration();

        // Initialize admin account if not exists
        initializeAdminAccount();

        // Launch GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel to system default
                javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Get screen dimensions for positioning
            java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = screenSize.width;
            int screenHeight = screenSize.height;

            // Create two login windows
            LoginFrame studentLoginFrame = new LoginFrame("Student Login");
            LoginFrame adminLoginFrame = new LoginFrame("Admin Login");

            // Position them side by side
            int loginWidth = 400;
            int loginHeight = 350;
            int spacing = 20;
            
            // Center the two windows with spacing between them
            int totalWidth = (loginWidth * 2) + spacing;
            int startX = (screenWidth - totalWidth) / 2;
            int startY = (screenHeight - loginHeight) / 2;

            // Student login on the left
            studentLoginFrame.setLocation(startX, startY);
            studentLoginFrame.setSize(loginWidth, loginHeight);
            studentLoginFrame.setVisible(true);

            // Admin login on the right
            adminLoginFrame.setLocation(startX + loginWidth + spacing, startY);
            adminLoginFrame.setSize(loginWidth, loginHeight);
            adminLoginFrame.setVisible(true);
        });
    }

    private static void initializeDataDirectory() {
        File dataDir = new File(Constants.DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
            System.out.println("Created data directory");
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    private static void initializeConfiguration() {
        try {
            ConfigurationDAO configDAO = new ConfigurationDAO();
            configDAO.loadConfiguration(); // Will create default if not exists
            System.out.println("Configuration initialized");
        } catch (Exception e) {
            System.err.println("Error initializing configuration: " + e.getMessage());
        }
    }
    @SuppressWarnings("UseSpecificCatch")
    private static void initializeAdminAccount() {
        try {
            StudentDAO studentDAO = new StudentDAO();
            // Will create admin account if not exists
            studentDAO.loadAdmin();
            System.out.println("Admin account initialized");
        } catch (Exception e) {
            System.err.println("Error initializing admin account: " + e.getMessage());
        }
    }
}
