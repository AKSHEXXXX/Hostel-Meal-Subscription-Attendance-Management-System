package com.hostelms.observer;

import com.hostelms.model.Notification;
import com.hostelms.model.Student;

import java.util.HashMap;
import java.util.Map;

public class NotificationCenter implements Subject {
    // Singleton instance
    private static NotificationCenter instance;

    // Observers mapped by roll number
    private Map<String, Observer> observers;

    // Private constructor for Singleton
    private NotificationCenter() {
        observers = new HashMap<>();
    }

    // Singleton getInstance method
    public static synchronized NotificationCenter getInstance() {
        if (instance == null) {
            instance = new NotificationCenter();
        }
        return instance;
    }

    @Override
    public void attach(Observer observer) {
        if (observer instanceof Student) {
            Student student = (Student) observer;
            observers.put(student.getRollNumber(), observer);
        }
    }

    @Override
    public void detach(Observer observer) {
        if (observer instanceof Student) {
            Student student = (Student) observer;
            observers.remove(student.getRollNumber());
        }
    }

    @Override
    public void notifyObservers(Notification notification) {
        // Notify all observers
        for (Observer observer : observers.values()) {
            observer.update(notification);
        }
    }

    // Notify specific student by roll number
    public void notifyStudent(String rollNumber, Notification notification) {
        Observer observer = observers.get(rollNumber);
        if (observer != null) {
            observer.update(notification);
        }
    }
}
