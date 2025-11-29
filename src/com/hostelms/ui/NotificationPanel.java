package com.hostelms.ui;

import com.hostelms.model.Notification;
import com.hostelms.model.Student;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NotificationPanel extends JPanel {
    private Student student;
    private JList<String> notificationList;
    private DefaultListModel<String> listModel;

    public NotificationPanel(Student student) {
        this.student = student;
        initComponents();
        loadNotifications();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Notifications", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // List
        listModel = new DefaultListModel<>();
        notificationList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(notificationList);
        add(scrollPane, BorderLayout.CENTER);

        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadNotifications());
        add(refreshButton, BorderLayout.SOUTH);
    }

    private void loadNotifications() {
        listModel.clear();
        List<Notification> notifications = student.getNotifications();

        if (notifications.isEmpty()) {
            listModel.addElement("No notifications");
        } else {
            for (int i = notifications.size() - 1; i >= 0; i--) {
                Notification notification = notifications.get(i);
                String item = String.format("[%s] %s - %s",
                    notification.getType(),
                    notification.getTimestamp().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    notification.getMessage());
                listModel.addElement(item);
            }
        }
    }
}
