package com.hostelms.service;

import com.hostelms.model.Notification;
import com.hostelms.observer.NotificationCenter;

public class NotificationService {
    public void sendNotification(String rollNumber, String message, String type) {
        Notification notification = new Notification(message, type);
        NotificationCenter.getInstance().notifyStudent(rollNumber, notification);
    }

    public void broadcastNotification(String message, String type) {
        Notification notification = new Notification(message, type);
        NotificationCenter.getInstance().notifyObservers(notification);
    }
}
