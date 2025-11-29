package com.hostelms.model;

import java.time.LocalDateTime;

public class Notification {
    private String message;
    private LocalDateTime timestamp;
    private boolean read;
    private String type; // INFO, WARNING, SUCCESS, ERROR

    public Notification(String message, String type) {
        this.message = message;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.read = false;
    }

    public void markAsRead() {
        this.read = true;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
