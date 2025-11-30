package com.hostelms.ui;

import com.hostelms.model.MealRecord;
import com.hostelms.model.MealType;
import com.hostelms.model.Student;
import com.hostelms.service.MealService;
import com.hostelms.util.DataAccessException;
import com.hostelms.util.MealSelectionException;
import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;

public class MealSelectionPanel extends JPanel {
    private final Student student;
    private final MealService mealService;

    private JCheckBox breakfastCheckBox;
    private JCheckBox lunchCheckBox;
    private JCheckBox dinnerCheckBox;
    private JButton selectButton;
    private JLabel statusLabel;
    private JLabel dateLabel;

    public MealSelectionPanel(Student student, StudentDashboard dashboard) {
        this.student = student;
        this.mealService = new MealService();

        initComponents();
        loadTomorrowMeals();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title panel
        JPanel titlePanel = new JPanel(new GridLayout(3, 1));
        JLabel titleLabel = new JLabel("Select Meals for Tomorrow", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel);

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        dateLabel = new JLabel("Date: " + tomorrow, JLabel.CENTER);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titlePanel.add(dateLabel);

        // Add balance label
        JLabel balanceLabel = new JLabel(
            String.format("Current Balance: AED %.2f", student.getBalance()), 
            JLabel.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        balanceLabel.setForeground(new Color(0, 100, 0));
        titlePanel.add(balanceLabel);

        add(titlePanel, BorderLayout.NORTH);

        // Meal selection panel
        JPanel mealsPanel = new JPanel(new GridLayout(4, 1, 10, 10));

        breakfastCheckBox = new JCheckBox("Breakfast (AED 30.00)");
        lunchCheckBox = new JCheckBox("Lunch (AED 50.00)");
        dinnerCheckBox = new JCheckBox("Dinner (AED 40.00)");

        mealsPanel.add(breakfastCheckBox);
        mealsPanel.add(lunchCheckBox);
        mealsPanel.add(dinnerCheckBox);

        selectButton = new JButton("Update Selection");
        selectButton.addActionListener(e -> handleUpdateSelection());
        mealsPanel.add(selectButton);

        add(mealsPanel, BorderLayout.CENTER);

        // Status label
        statusLabel = new JLabel("", JLabel.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void loadTomorrowMeals() {
        try {
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            MealRecord record = mealService.getMealRecord(student.getRollNumber(), tomorrow);

            if (record != null) {
                breakfastCheckBox.setSelected(record.isBreakfastSelected());
                lunchCheckBox.setSelected(record.isLunchSelected());
                dinnerCheckBox.setSelected(record.isDinnerSelected());
            }
        } catch (DataAccessException ex) {
            statusLabel.setText("Error loading meal selection");
            statusLabel.setForeground(Color.RED);
        }
    }

    private void handleUpdateSelection() {
        try {
            LocalDate tomorrow = LocalDate.now().plusDays(1);

            // Calculate total cost for selected meals
            double totalCost = 0.0;
            if (breakfastCheckBox.isSelected()) totalCost += 30.00;
            if (lunchCheckBox.isSelected()) totalCost += 50.00;
            if (dinnerCheckBox.isSelected()) totalCost += 40.00;

            // Check if student has sufficient balance
            if (totalCost > student.getBalance()) {
                statusLabel.setText(String.format(
                    "Insufficient balance! Required: AED %.2f, Available: AED %.2f",
                    totalCost, student.getBalance()));
                statusLabel.setForeground(Color.RED);
                return;
            }

            // Update breakfast
            if (breakfastCheckBox.isSelected()) {
                mealService.selectMeal(student.getRollNumber(), tomorrow, MealType.BREAKFAST);
            } else {
                mealService.cancelMeal(student.getRollNumber(), tomorrow, MealType.BREAKFAST);
            }

            // Update lunch
            if (lunchCheckBox.isSelected()) {
                mealService.selectMeal(student.getRollNumber(), tomorrow, MealType.LUNCH);
            } else {
                mealService.cancelMeal(student.getRollNumber(), tomorrow, MealType.LUNCH);
            }

            // Update dinner
            if (dinnerCheckBox.isSelected()) {
                mealService.selectMeal(student.getRollNumber(), tomorrow, MealType.DINNER);
            } else {
                mealService.cancelMeal(student.getRollNumber(), tomorrow, MealType.DINNER);
            }

            statusLabel.setText("Meal selection updated successfully");
            statusLabel.setForeground(new Color(0, 128, 0));

        } catch (MealSelectionException | DataAccessException ex) {
            statusLabel.setText(ex.getMessage());
            statusLabel.setForeground(Color.RED);
        }
    }
}
