package com.hostelms.model;

public enum MealType {
    BREAKFAST("Breakfast", "08:00"),
    LUNCH("Lunch", "13:00"),
    DINNER("Dinner", "20:00");

    private final String displayName;
    private final String defaultTime;

    MealType(String displayName, String defaultTime) {
        this.displayName = displayName;
        this.defaultTime = defaultTime;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDefaultTime() {
        return defaultTime;
    }
}
