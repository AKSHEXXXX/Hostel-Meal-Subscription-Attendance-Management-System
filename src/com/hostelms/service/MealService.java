package com.hostelms.service;

import com.hostelms.dao.MealRecordDAO;
import com.hostelms.model.MealRecord;
import com.hostelms.model.MealType;
import com.hostelms.model.Notification;
import com.hostelms.observer.NotificationCenter;
import com.hostelms.util.DataAccessException;
import com.hostelms.util.MealSelectionException;
import com.hostelms.util.ValidationUtil;
import java.time.LocalDate;
import java.util.List;

public class MealService {
    private final MealRecordDAO mealRecordDAO;

    public MealService() {
        this.mealRecordDAO = new MealRecordDAO();
    }

    public void selectMeal(String rollNumber, LocalDate date, MealType mealType)
        throws MealSelectionException, DataAccessException {

        // Validate date (cutoff time removed for demonstration)
        ValidationUtil.validateMealSelection(date);

        // Load or create meal record
        MealRecord record = mealRecordDAO.findByRollNumberAndDate(rollNumber, date);
        if (record == null) {
            record = new MealRecord(rollNumber, date);
        }

        record.selectMeal(mealType);
        mealRecordDAO.saveMealRecord(record);

        // Notify student
        Notification notification = new Notification(
            mealType + " selected for " + date, "SUCCESS");
        NotificationCenter.getInstance().notifyStudent(rollNumber, notification);
    }

    public void cancelMeal(String rollNumber, LocalDate date, MealType mealType)
        throws MealSelectionException, DataAccessException {

        // Validate date (cutoff time removed for demonstration)
        ValidationUtil.validateMealSelection(date);

        // Load meal record
        MealRecord record = mealRecordDAO.findByRollNumberAndDate(rollNumber, date);
        if (record == null) {
            return; // Nothing to cancel
        }

        record.cancelMeal(mealType);
        mealRecordDAO.saveMealRecord(record);

        // Notify student
        Notification notification = new Notification(
            mealType + " cancelled for " + date, "INFO");
        NotificationCenter.getInstance().notifyStudent(rollNumber, notification);
    }

    public MealRecord getMealRecord(String rollNumber, LocalDate date)
        throws DataAccessException {
        return mealRecordDAO.findByRollNumberAndDate(rollNumber, date);
    }

    public List<MealRecord> getAllMealRecordsForDate(LocalDate date)
        throws DataAccessException {
        return mealRecordDAO.findByDate(date);
    }
}
