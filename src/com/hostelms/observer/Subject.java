package com.hostelms.observer;

import com.hostelms.model.Notification;

public interface Subject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(Notification notification);
}
