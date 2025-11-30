package com.hostelms.ui;

import com.hostelms.dao.StudentDAO;
import com.hostelms.model.MealRecord;
import com.hostelms.model.MealType;
import com.hostelms.model.Student;
import com.hostelms.service.AttendanceService;
import com.hostelms.service.MealService;
import com.hostelms.util.DataAccessException;
import com.hostelms.util.InsufficientBalanceException;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AttendancePanel extends JPanel {
    private final AttendanceService attendanceService;
    private final MealService mealService;

    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> dateComboBox;
    private JButton loadButton;
    private JButton saveButton;

    public AttendancePanel() {
        this.attendanceService = new AttendanceService();
        this.mealService = new MealService();

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel - Date selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Date:"));

        dateComboBox = new JComboBox<>();
        dateComboBox.addItem(LocalDate.now().plusDays(1).toString());  // Tomorrow
        dateComboBox.addItem(LocalDate.now().toString());              // Today
        dateComboBox.addItem(LocalDate.now().minusDays(1).toString()); // Yesterday
        topPanel.add(dateComboBox);

        loadButton = new JButton("Load Meal Selections");
        loadButton.addActionListener(e -> loadMealSelections());
        topPanel.add(loadButton);

        add(topPanel, BorderLayout.NORTH);

        // Table - Student attendance
        String[] columns = {"Roll Number", "Name", "Breakfast", "B-Attend",
            "Lunch", "L-Attend", "Dinner", "D-Attend"};
        tableModel = new DefaultTableModel(columns, 0);
        attendanceTable = new JTable(tableModel);

        // Combo box editors for attendance columns
        String[] attendanceOptions = {"PRESENT", "ABSENT", "NOT_SELECTED"};

        attendanceTable.getColumnModel().getColumn(3).setCellEditor(
            new DefaultCellEditor(new JComboBox<>(attendanceOptions)));
        attendanceTable.getColumnModel().getColumn(5).setCellEditor(
            new DefaultCellEditor(new JComboBox<>(attendanceOptions)));
        attendanceTable.getColumnModel().getColumn(7).setCellEditor(
            new DefaultCellEditor(new JComboBox<>(attendanceOptions)));

        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel - Save button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        saveButton = new JButton("Save Attendance & Process Charges");
        saveButton.addActionListener(e -> saveAttendance());
        saveButton.setEnabled(false);
        bottomPanel.add(saveButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadMealSelections() {
        try {
            String dateStr = (String) dateComboBox.getSelectedItem();
            LocalDate date = LocalDate.parse(dateStr);

            List<MealRecord> records = mealService.getAllMealRecordsForDate(date);

            if (records.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No meal selections found for this date.",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
                tableModel.setRowCount(0);
                saveButton.setEnabled(false);
                return;
            }

            // Clear table
            tableModel.setRowCount(0);

            // Load meal selections
            StudentDAO studentDAO = new StudentDAO();
            for (MealRecord record : records) {
                Student student = studentDAO.findByRollNumber(record.getRollNumber());
                if (student != null) {
                    tableModel.addRow(new Object[]{
                        student.getRollNumber(),
                        student.getName(),
                        record.isBreakfastSelected() ? "Yes" : "No",
                        record.isBreakfastSelected() ? "PRESENT" : "NOT_SELECTED",
                        record.isLunchSelected() ? "Yes" : "No",
                        record.isLunchSelected() ? "PRESENT" : "NOT_SELECTED",
                        record.isDinnerSelected() ? "Yes" : "No",
                        record.isDinnerSelected() ? "PRESENT" : "NOT_SELECTED"
                    });
                }
            }

            saveButton.setEnabled(true);

        } catch (DataAccessException ex) {
            JOptionPane.showMessageDialog(this,
                "Error loading meal selections: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveAttendance() {
        try {
            String dateStr = (String) dateComboBox.getSelectedItem();
            LocalDate date = LocalDate.parse(dateStr);

            // Validate date - only allow today
            LocalDate today = LocalDate.now();
            
            if (!date.equals(today)) {
                JOptionPane.showMessageDialog(this,
                    "Attendance can only be saved for today (" + today + ").",
                    "Invalid Date", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Map<String, Map<MealType, String>> attendanceData = new HashMap<>();

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String rollNumber = (String) tableModel.getValueAt(i, 0);

                Map<MealType, String> studentAttendance = new HashMap<>();
                studentAttendance.put(MealType.BREAKFAST,
                    (String) tableModel.getValueAt(i, 3));
                studentAttendance.put(MealType.LUNCH,
                    (String) tableModel.getValueAt(i, 5));
                studentAttendance.put(MealType.DINNER,
                    (String) tableModel.getValueAt(i, 7));

                attendanceData.put(rollNumber, studentAttendance);
            }

            attendanceService.markAttendance(date, attendanceData);

            JOptionPane.showMessageDialog(this,
                "Attendance saved and charges processed successfully",
                "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (DataAccessException | InsufficientBalanceException ex) {
            JOptionPane.showMessageDialog(this,
                "Error saving attendance: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
