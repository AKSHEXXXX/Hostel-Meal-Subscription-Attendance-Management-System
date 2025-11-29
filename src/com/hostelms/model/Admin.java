package com.hostelms.model;

public class Admin extends User {
    public Admin(String rollNumber, String name, String password) {
        super(rollNumber, name, password);
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }
}
