package com.hostelms.ui;

import com.hostelms.service.ReportService;
import com.hostelms.util.DataAccessException;
import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;

public class ReportPanel extends JPanel {
    private final ReportService reportService;

    private JComboBox<String> dateComboBox;
    private JTextArea reportTextArea;
    private JButton generateButton;

    public ReportPanel() {
        this.reportService = new ReportService();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Date:"));

        dateComboBox = new JComboBox<>();
        // Show Today first, then previous days
        dateComboBox.addItem(LocalDate.now().toString());              // Today
        dateComboBox.addItem(LocalDate.now().minusDays(1).toString()); // Yesterday
        dateComboBox.addItem(LocalDate.now().minusDays(2).toString()); // 2 days ago
        dateComboBox.addItem(LocalDate.now().minusDays(3).toString()); // 3 days ago
        dateComboBox.addItem(LocalDate.now().minusDays(4).toString()); // 4 days ago
        dateComboBox.addItem(LocalDate.now().minusDays(5).toString()); // 5 days ago
        dateComboBox.addItem(LocalDate.now().minusDays(6).toString()); // 6 days ago
        topPanel.add(dateComboBox);

        generateButton = new JButton("Generate Report");
        generateButton.addActionListener(e -> generateReport());
        topPanel.add(generateButton);

        add(topPanel, BorderLayout.NORTH);

        // Report text area
        reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);
        reportTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(reportTextArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void generateReport() {
        try {
            String dateStr = (String) dateComboBox.getSelectedItem();
            LocalDate date = LocalDate.parse(dateStr);

            String report = reportService.generateDailyReport(date);
            reportTextArea.setText(report);

        } catch (DataAccessException ex) {
            JOptionPane.showMessageDialog(this,
                "Error generating report: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
