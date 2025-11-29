package com.hostelms.ui;

import com.hostelms.dao.ConfigurationDAO;
import com.hostelms.model.MealConfiguration;
import com.hostelms.util.DataAccessException;

import javax.swing.*;
import java.awt.*;

public class ConfigurationPanel extends JPanel {
    private ConfigurationDAO configDAO;

    private JTextField breakfastPriceField;
    private JTextField lunchPriceField;
    private JTextField dinnerPriceField;
    private JComboBox<String> refundPolicyCombo;
    private JButton saveButton;

    public ConfigurationPanel() {
        this.configDAO = new ConfigurationDAO();
        initComponents();
        loadConfiguration();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("System Configuration", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        formPanel.add(new JLabel("Breakfast Price (Rs.):"));
        breakfastPriceField = new JTextField();
        formPanel.add(breakfastPriceField);

        formPanel.add(new JLabel("Lunch Price (Rs.):"));
        lunchPriceField = new JTextField();
        formPanel.add(lunchPriceField);

        formPanel.add(new JLabel("Dinner Price (Rs.):"));
        dinnerPriceField = new JTextField();
        formPanel.add(dinnerPriceField);

        formPanel.add(new JLabel("Refund Policy:"));
        String[] policies = {"FULL", "EIGHTY_PERCENT", "FIFTY_PERCENT"};
        refundPolicyCombo = new JComboBox<>(policies);
        formPanel.add(refundPolicyCombo);

        formPanel.add(new JLabel("")); // Spacer
        saveButton = new JButton("Save Configuration");
        saveButton.addActionListener(e -> saveConfiguration());
        formPanel.add(saveButton);

        add(formPanel, BorderLayout.CENTER);
    }

    private void loadConfiguration() {
        try {
            MealConfiguration config = configDAO.loadConfiguration();

            breakfastPriceField.setText(String.valueOf(config.getBreakfastPrice()));
            lunchPriceField.setText(String.valueOf(config.getLunchPrice()));
            dinnerPriceField.setText(String.valueOf(config.getDinnerPrice()));
            refundPolicyCombo.setSelectedItem(config.getRefundPolicy());

        } catch (DataAccessException ex) {
            JOptionPane.showMessageDialog(this,
                "Error loading configuration: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveConfiguration() {
        try {
            MealConfiguration config = new MealConfiguration();
            config.setBreakfastPrice(Double.parseDouble(breakfastPriceField.getText()));
            config.setLunchPrice(Double.parseDouble(lunchPriceField.getText()));
            config.setDinnerPrice(Double.parseDouble(dinnerPriceField.getText()));
            config.setRefundPolicy((String) refundPolicyCombo.getSelectedItem());

            // Keep existing time settings
            MealConfiguration existing = configDAO.loadConfiguration();
            config.setMealCutoffTime(existing.getMealCutoffTime());
            config.setBreakfastTime(existing.getBreakfastTime());
            config.setLunchTime(existing.getLunchTime());
            config.setDinnerTime(existing.getDinnerTime());

            configDAO.saveConfiguration(config);

            JOptionPane.showMessageDialog(this,
                "Configuration saved successfully",
                "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Invalid price format. Please enter valid numbers.",
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (DataAccessException ex) {
            JOptionPane.showMessageDialog(this,
                "Error saving configuration: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
