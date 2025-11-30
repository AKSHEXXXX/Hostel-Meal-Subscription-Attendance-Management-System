package com.hostelms.service;

import com.hostelms.dao.MealRecordDAO;
import com.hostelms.dao.StudentDAO;
import com.hostelms.dao.TransactionDAO;
import com.hostelms.model.MealRecord;
import com.hostelms.model.Student;
import com.hostelms.model.Transaction;
import com.hostelms.model.TransactionType;
import com.hostelms.util.DataAccessException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportService {
    private MealRecordDAO mealRecordDAO;
    private TransactionDAO transactionDAO;
    private StudentDAO studentDAO;

    public ReportService() {
        this.mealRecordDAO = new MealRecordDAO();
        this.transactionDAO = new TransactionDAO();
        this.studentDAO = new StudentDAO();
    }

    public String generateDailyReport(LocalDate date) throws DataAccessException {
        StringBuilder report = new StringBuilder();
        report.append("Daily Report for ").append(date).append("\n");
        report.append("=".repeat(50)).append("\n\n");

        List<MealRecord> records = mealRecordDAO.findByDate(date);

        int breakfastCount = 0, lunchCount = 0, dinnerCount = 0;
        for (MealRecord record : records) {
            if (record.isBreakfastSelected()) breakfastCount++;
            if (record.isLunchSelected()) lunchCount++;
            if (record.isDinnerSelected()) dinnerCount++;
        }

        report.append("Meal Selections:\n");
        report.append("Breakfast: ").append(breakfastCount).append("\n");
        report.append("Lunch: ").append(lunchCount).append("\n");
        report.append("Dinner: ").append(dinnerCount).append("\n\n");

        // Add transaction summary
        List<Transaction> transactions = transactionDAO.findByDateRange(date, date);
        double totalCharges = 0, totalRefunds = 0;

        for (Transaction txn : transactions) {
            if (txn.getType() == TransactionType.CHARGE) {
                totalCharges += txn.getAmount();
            } else if (txn.getType() == TransactionType.REFUND) {
                totalRefunds += txn.getAmount();
            }
        }

        report.append("Financial Summary:\n");
        report.append("Total Charges: AED ").append(String.format("%.2f", totalCharges)).append("\n");
        report.append("Total Refunds: AED ").append(String.format("%.2f", totalRefunds)).append("\n");
        report.append("Net Revenue: AED ").append(String.format("%.2f", totalCharges - totalRefunds)).append("\n");

        return report.toString();
    }

    public String generateExpectedMealReport(LocalDate date) throws DataAccessException {
        StringBuilder report = new StringBuilder();
        report.append("Expected Meal Report for ").append(date).append("\n");
        report.append("=".repeat(50)).append("\n\n");

        List<MealRecord> records = mealRecordDAO.findByDate(date);

        if (records.isEmpty()) {
            report.append("No meal selections found for this date.\n");
            return report.toString();
        }

        int breakfastCount = 0, lunchCount = 0, dinnerCount = 0;
        double breakfastRevenue = 0, lunchRevenue = 0, dinnerRevenue = 0;

        // Fixed meal prices
        final double BREAKFAST_PRICE = 30.00;
        final double LUNCH_PRICE = 50.00;
        final double DINNER_PRICE = 40.00;

        for (MealRecord record : records) {
            if (record.isBreakfastSelected()) {
                breakfastCount++;
                breakfastRevenue += BREAKFAST_PRICE;
            }
            if (record.isLunchSelected()) {
                lunchCount++;
                lunchRevenue += LUNCH_PRICE;
            }
            if (record.isDinnerSelected()) {
                dinnerCount++;
                dinnerRevenue += DINNER_PRICE;
            }
        }

        report.append("Expected Meal Counts:\n");
        report.append("-".repeat(50)).append("\n");
        report.append(String.format("%-20s %10s %15s\n", "Meal Type", "Count", "Revenue"));
        report.append("-".repeat(50)).append("\n");
        report.append(String.format("%-20s %10d     AED %10.2f\n", "Breakfast", breakfastCount, breakfastRevenue));
        report.append(String.format("%-20s %10d     AED %10.2f\n", "Lunch", lunchCount, lunchRevenue));
        report.append(String.format("%-20s %10d     AED %10.2f\n", "Dinner", dinnerCount, dinnerRevenue));
        report.append("-".repeat(50)).append("\n");

        int totalMeals = breakfastCount + lunchCount + dinnerCount;
        double totalRevenue = breakfastRevenue + lunchRevenue + dinnerRevenue;

        report.append(String.format("%-20s %10d     AED %10.2f\n", "TOTAL", totalMeals, totalRevenue));
        report.append("\n");

        report.append("Total Students: ").append(records.size()).append("\n");
        report.append("Expected Revenue: AED ").append(String.format("%.2f", totalRevenue)).append("\n");

        return report.toString();
    }

    public boolean isAttendanceSaved(LocalDate date) throws DataAccessException {
        List<MealRecord> records = mealRecordDAO.findByDate(date);
        
        if (records.isEmpty()) {
            return false;
        }

        // Check if any meal record has been completed (attendance saved)
        for (MealRecord record : records) {
            if ("COMPLETED".equals(record.getStatus())) {
                return true;
            }
        }

        return false;
    }

    public List<String[]> getMealSelectionTableData(LocalDate date)
        throws DataAccessException {
        List<MealRecord> records = mealRecordDAO.findByDate(date);
        List<String[]> tableData = new ArrayList<>();

        for (MealRecord record : records) {
            Student student = studentDAO.findByRollNumber(record.getRollNumber());
            if (student != null) {
                String[] row = {
                    student.getRollNumber(),
                    student.getName(),
                    record.isBreakfastSelected() ? "Yes" : "No",
                    record.isLunchSelected() ? "Yes" : "No",
                    record.isDinnerSelected() ? "Yes" : "No",
                    String.valueOf(record.getTotalMealsSelected())
                };
                tableData.add(row);
            }
        }

        return tableData;
    }
}
