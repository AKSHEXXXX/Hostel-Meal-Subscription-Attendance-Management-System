package com.hostelms.dao;

import com.hostelms.model.MealConfiguration;
import com.hostelms.util.Constants;
import com.hostelms.util.DataAccessException;
import com.hostelms.util.DateTimeUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationDAO {
    private static final String FILE_PATH = Constants.CONFIG_FILE;

    public MealConfiguration loadConfiguration() throws DataAccessException {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return createDefaultConfiguration();
        }

        Map<String, String> configMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length == 2) {
                    configMap.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            throw new DataAccessException("Error reading configuration file", e);
        }

        MealConfiguration config = new MealConfiguration();

        try {
            config.setBreakfastPrice(Double.parseDouble(
                configMap.getOrDefault("breakfastPrice",
                    String.valueOf(Constants.DEFAULT_BREAKFAST_PRICE))));

            config.setLunchPrice(Double.parseDouble(
                configMap.getOrDefault("lunchPrice",
                    String.valueOf(Constants.DEFAULT_LUNCH_PRICE))));

            config.setDinnerPrice(Double.parseDouble(
                configMap.getOrDefault("dinnerPrice",
                    String.valueOf(Constants.DEFAULT_DINNER_PRICE))));

            config.setRefundPolicy(
                configMap.getOrDefault("refundPolicy", Constants.DEFAULT_REFUND_POLICY));

            config.setMealCutoffTime(DateTimeUtil.parseTime(
                configMap.getOrDefault("mealCutoffTime", Constants.DEFAULT_CUTOFF_TIME)));

            config.setBreakfastTime(DateTimeUtil.parseTime(
                configMap.getOrDefault("breakfastTime", "08:00")));

            config.setLunchTime(DateTimeUtil.parseTime(
                configMap.getOrDefault("lunchTime", "13:00")));

            config.setDinnerTime(DateTimeUtil.parseTime(
                configMap.getOrDefault("dinnerTime", "20:00")));

        } catch (Exception e) {
            System.err.println("Error parsing configuration, using defaults: " + e.getMessage());
            return createDefaultConfiguration();
        }

        return config;
    }

    public void saveConfiguration(MealConfiguration config) throws DataAccessException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("breakfastPrice," + config.getBreakfastPrice());
            writer.newLine();

            writer.write("lunchPrice," + config.getLunchPrice());
            writer.newLine();

            writer.write("dinnerPrice," + config.getDinnerPrice());
            writer.newLine();

            writer.write("refundPolicy," + config.getRefundPolicy());
            writer.newLine();

            writer.write("mealCutoffTime," + DateTimeUtil.formatTime(config.getMealCutoffTime()));
            writer.newLine();

            writer.write("breakfastTime," + DateTimeUtil.formatTime(config.getBreakfastTime()));
            writer.newLine();

            writer.write("lunchTime," + DateTimeUtil.formatTime(config.getLunchTime()));
            writer.newLine();

            writer.write("dinnerTime," + DateTimeUtil.formatTime(config.getDinnerTime()));
            writer.newLine();

        } catch (IOException e) {
            throw new DataAccessException("Error writing configuration file", e);
        }
    }

    private MealConfiguration createDefaultConfiguration() throws DataAccessException {
        MealConfiguration config = new MealConfiguration();
        config.setBreakfastPrice(Constants.DEFAULT_BREAKFAST_PRICE);
        config.setLunchPrice(Constants.DEFAULT_LUNCH_PRICE);
        config.setDinnerPrice(Constants.DEFAULT_DINNER_PRICE);
        config.setRefundPolicy(Constants.DEFAULT_REFUND_POLICY);
        config.setMealCutoffTime(DateTimeUtil.parseTime(Constants.DEFAULT_CUTOFF_TIME));
        config.setBreakfastTime(DateTimeUtil.parseTime("08:00"));
        config.setLunchTime(DateTimeUtil.parseTime("13:00"));
        config.setDinnerTime(DateTimeUtil.parseTime("20:00"));

        // Save default configuration
        saveConfiguration(config);

        return config;
    }
}
