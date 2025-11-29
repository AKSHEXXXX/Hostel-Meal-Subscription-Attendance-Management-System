package com.hostelms.dao;

import com.hostelms.model.MealType;
import com.hostelms.util.Constants;
import com.hostelms.util.DataAccessException;
import com.hostelms.util.DateTimeUtil;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class AttendanceDAO {
    private static final String FILE_PATH = Constants.ATTENDANCE_FILE;

    public void saveAttendanceRecord(String rollNumber, LocalDate date,
                                    Map<MealType, String> attendance) throws DataAccessException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(attendanceToString(rollNumber, date, attendance));
            writer.newLine();
        } catch (IOException e) {
            throw new DataAccessException("Error writing attendance file", e);
        }
    }

    public Map<MealType, String> findAttendance(String rollNumber, LocalDate date)
        throws DataAccessException {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String roll = parts[0].trim();
                    LocalDate lineDate = DateTimeUtil.parseDate(parts[1].trim());

                    if (roll.equals(rollNumber) && lineDate.equals(date)) {
                        Map<MealType, String> attendance = new HashMap<>();
                        attendance.put(MealType.BREAKFAST, parts[2].trim());
                        attendance.put(MealType.LUNCH, parts[3].trim());
                        attendance.put(MealType.DINNER, parts[4].trim());
                        return attendance;
                    }
                }
            }
        } catch (IOException e) {
            throw new DataAccessException("Error reading attendance file", e);
        }

        return null;
    }

    public boolean attendanceExists(LocalDate date) throws DataAccessException {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    LocalDate lineDate = DateTimeUtil.parseDate(parts[1].trim());
                    if (lineDate.equals(date)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            throw new DataAccessException("Error checking attendance file", e);
        }

        return false;
    }

    // Helper methods
    private String attendanceToString(String rollNumber, LocalDate date,
                                     Map<MealType, String> attendance) {
        return String.format("%s,%s,%s,%s,%s,%s",
            rollNumber,
            DateTimeUtil.formatDate(date),
            attendance.get(MealType.BREAKFAST),
            attendance.get(MealType.LUNCH),
            attendance.get(MealType.DINNER),
            DateTimeUtil.formatDateTime(LocalDateTime.now()));
    }
}
