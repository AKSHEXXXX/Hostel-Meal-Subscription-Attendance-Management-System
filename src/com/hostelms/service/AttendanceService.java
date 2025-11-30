package com.hostelms.service;

import com.hostelms.dao.AttendanceDAO;
import com.hostelms.dao.ConfigurationDAO;
import com.hostelms.dao.MealRecordDAO;
import com.hostelms.model.*;
import com.hostelms.policy.*;
import com.hostelms.util.DataAccessException;
import com.hostelms.util.InsufficientBalanceException;

import java.time.LocalDate;
import java.util.Map;

public class AttendanceService {
    private AttendanceDAO attendanceDAO;
    private MealRecordDAO mealRecordDAO;
    private TransactionService transactionService;
    private ConfigurationDAO configDAO;

    public AttendanceService() {
        this.attendanceDAO = new AttendanceDAO();
        this.mealRecordDAO = new MealRecordDAO();
        this.transactionService = new TransactionService();
        this.configDAO = new ConfigurationDAO();
    }

    public void markAttendance(LocalDate date, Map<String, Map<MealType, String>> attendanceData)
        throws DataAccessException, InsufficientBalanceException {

        // Validate date - only allow today
        LocalDate today = LocalDate.now();
        
        if (!date.equals(today)) {
            throw new DataAccessException(
                "Attendance can only be saved for today (" + today + ").");
        }

        MealConfiguration config = configDAO.loadConfiguration();
        RefundPolicy refundPolicy = getRefundPolicy(config.getRefundPolicy());

        for (Map.Entry<String, Map<MealType, String>> entry : attendanceData.entrySet()) {
            String rollNumber = entry.getKey();
            Map<MealType, String> studentAttendance = entry.getValue();

            // Check if attendance already processed for this date
            // by checking if meal record status is already "COMPLETED"
            MealRecord existingRecord = mealRecordDAO.findByRollNumberAndDate(rollNumber, date);
            if (existingRecord != null && "COMPLETED".equals(existingRecord.getStatus())) {
                throw new DataAccessException(
                    "Attendance already processed for " + date + ". Cannot process again.");
            }

            // Save attendance
            attendanceDAO.saveAttendanceRecord(rollNumber, date, studentAttendance);

            // Process charges and refunds
            processChargesAndRefunds(rollNumber, date, studentAttendance, config, refundPolicy);
        }
    }

    private void processChargesAndRefunds(String rollNumber, LocalDate date,
                                         Map<MealType, String> attendance,
                                         MealConfiguration config, RefundPolicy refundPolicy)
        throws DataAccessException, InsufficientBalanceException {

        MealRecord mealRecord = mealRecordDAO.findByRollNumberAndDate(rollNumber, date);
        if (mealRecord == null) {
            return; // No meal selections for this student
        }

        for (MealType mealType : MealType.values()) {
            boolean selected = mealRecord.isMealSelected(mealType);
            String attendanceStatus = attendance.get(mealType);

            if (selected && "PRESENT".equals(attendanceStatus)) {
                // Charge for meal
                double price = config.getMealPrice(mealType);
                transactionService.chargeMeal(rollNumber, date, mealType, price);

            } else if (selected && "ABSENT".equals(attendanceStatus)) {
                // Refund for missed meal
                double price = config.getMealPrice(mealType);
                double refundAmount = refundPolicy.calculateRefund(price);
                transactionService.refundMeal(rollNumber, date, mealType, refundAmount,
                    refundPolicy.getPolicyName());
            }
        }

        // Update meal record status
        mealRecord.setStatus("COMPLETED");
        mealRecordDAO.updateMealRecord(mealRecord);
    }

    private RefundPolicy getRefundPolicy(String policyName) {
        return switch (policyName) {
            case "FULL" -> new FullRefundPolicy();
            case "EIGHTY_PERCENT" -> new EightyPercentRefundPolicy();
            case "FIFTY_PERCENT" -> new FiftyPercentRefundPolicy();
            default -> new EightyPercentRefundPolicy();
        };
    }
}
