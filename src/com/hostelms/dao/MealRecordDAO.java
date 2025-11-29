package com.hostelms.dao;

import com.hostelms.model.MealRecord;
import com.hostelms.util.Constants;
import com.hostelms.util.DataAccessException;
import com.hostelms.util.DateTimeUtil;
import com.hostelms.util.FileCorruptionException;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MealRecordDAO {
    private static final String FILE_PATH = Constants.MEALS_FILE;

    public List<MealRecord> loadAllMealRecords() throws DataAccessException {
        List<MealRecord> records = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return records;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                try {
                    MealRecord record = parseMealRecord(line);
                    records.add(record);
                } catch (FileCorruptionException e) {
                    System.err.println("Skipping corrupted line: " + line);
                }
            }
        } catch (IOException e) {
            throw new DataAccessException("Error reading meals file", e);
        }

        return records;
    }

    public MealRecord findByRollNumberAndDate(String rollNumber, LocalDate date)
        throws DataAccessException {
        List<MealRecord> records = loadAllMealRecords();
        for (MealRecord record : records) {
            if (record.getRollNumber().equals(rollNumber) && record.getDate().equals(date)) {
                return record;
            }
        }
        return null;
    }

    public List<MealRecord> findByDate(LocalDate date) throws DataAccessException {
        List<MealRecord> allRecords = loadAllMealRecords();
        List<MealRecord> result = new ArrayList<>();

        for (MealRecord record : allRecords) {
            if (record.getDate().equals(date)) {
                result.add(record);
            }
        }

        return result;
    }

    public void saveMealRecord(MealRecord record) throws DataAccessException {
        List<MealRecord> records = loadAllMealRecords();

        // Check if record already exists
        boolean found = false;
        for (int i = 0; i < records.size(); i++) {
            MealRecord existing = records.get(i);
            if (existing.getRollNumber().equals(record.getRollNumber()) &&
                existing.getDate().equals(record.getDate())) {
                records.set(i, record);
                found = true;
                break;
            }
        }

        if (!found) {
            records.add(record);
        }

        writeAllMealRecords(records);
    }

    public void updateMealRecord(MealRecord record) throws DataAccessException {
        saveMealRecord(record); // Same as save
    }

    public void deleteMealRecord(String rollNumber, LocalDate date) throws DataAccessException {
        List<MealRecord> records = loadAllMealRecords();
        records.removeIf(record ->
            record.getRollNumber().equals(rollNumber) && record.getDate().equals(date));
        writeAllMealRecords(records);
    }

    // Helper methods
    private MealRecord parseMealRecord(String line) throws FileCorruptionException {
        String[] parts = line.split(",");
        if (parts.length != 6) {
            throw new FileCorruptionException("Invalid meal record format");
        }

        try {
            String rollNumber = parts[0].trim();
            LocalDate date = DateTimeUtil.parseDate(parts[1].trim());
            boolean breakfast = Boolean.parseBoolean(parts[2].trim());
            boolean lunch = Boolean.parseBoolean(parts[3].trim());
            boolean dinner = Boolean.parseBoolean(parts[4].trim());
            String status = parts[5].trim();

            MealRecord record = new MealRecord(rollNumber, date);
            record.setBreakfastSelected(breakfast);
            record.setLunchSelected(lunch);
            record.setDinnerSelected(dinner);
            record.setStatus(status);

            return record;
        } catch (Exception e) {
            throw new FileCorruptionException("Error parsing meal record: " + e.getMessage());
        }
    }

    private String mealRecordToString(MealRecord record) {
        return String.format("%s,%s,%s,%s,%s,%s",
            record.getRollNumber(),
            DateTimeUtil.formatDate(record.getDate()),
            record.isBreakfastSelected(),
            record.isLunchSelected(),
            record.isDinnerSelected(),
            record.getStatus());
    }

    private void writeAllMealRecords(List<MealRecord> records) throws DataAccessException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (MealRecord record : records) {
                writer.write(mealRecordToString(record));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DataAccessException("Error writing meals file", e);
        }
    }
}
