package com.hostelms.util;

import java.time.LocalDate;

public class ValidationUtil {
    public static boolean isValidRollNumber(String rollNumber) {
        return rollNumber != null && !rollNumber.trim().isEmpty();
    }

    public static boolean isValidPrice(double price) {
        return price >= 0;
    }

    public static boolean isValidBalance(double balance) {
        return balance >= 0;
    }

    public static void validateMealSelection(LocalDate date)
        throws InvalidDateException {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        if (!date.equals(tomorrow)) {
            throw new InvalidDateException("Can only select meals for tomorrow");
        }
        // Cutoff time check removed for demonstration purposes
    }
}
