package com.hostelms.model;

public abstract class User {
    protected String rollNumber;
    protected String name;
    protected String password;

    public User(String rollNumber, String name, String password) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.password = password;
    }

    public abstract String getRole();

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    // Getters and setters
    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
