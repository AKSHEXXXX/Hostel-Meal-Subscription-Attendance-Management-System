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
        report.append("Total Charges: Rs. ").append(String.format("%.2f", totalCharges)).append("\n");
        report.append("Total Refunds: Rs. ").append(String.format("%.2f", totalRefunds)).append("\n");
        report.append("Net Revenue: Rs. ").append(String.format("%.2f", totalCharges - totalRefunds)).append("\n");

        return report.toString();
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
