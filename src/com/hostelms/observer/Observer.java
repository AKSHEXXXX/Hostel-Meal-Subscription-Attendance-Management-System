package com.hostelms.observer;

import com.hostelms.model.Notification;

public interface Observer {
    void update(Notification notification);
}
