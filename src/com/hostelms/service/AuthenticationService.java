package com.hostelms.service;

import com.hostelms.dao.StudentDAO;
import com.hostelms.model.Admin;
import com.hostelms.model.Student;
import com.hostelms.model.User;
import com.hostelms.observer.NotificationCenter;
import com.hostelms.util.Constants;
import com.hostelms.util.DataAccessException;
import com.hostelms.util.InvalidCredentialsException;

public class AuthenticationService {
    private StudentDAO studentDAO;

    public AuthenticationService() {
        this.studentDAO = new StudentDAO();
    }

    public User login(String rollNumber, String password)
        throws InvalidCredentialsException, DataAccessException {

        if (rollNumber == null || rollNumber.trim().isEmpty()) {
            throw new InvalidCredentialsException("Roll number cannot be empty");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new InvalidCredentialsException("Password cannot be empty");
        }

        if (Constants.ADMIN_ROLL_NUMBER.equals(rollNumber)) {
            Admin admin = studentDAO.loadAdmin();
            if (admin != null && admin.authenticate(password)) {
                return admin;
            }
        } else {
            Student student = studentDAO.findByRollNumber(rollNumber);
            if (student != null && student.authenticate(password)) {
                NotificationCenter.getInstance().attach(student);
                return student;
            }
        }

        throw new InvalidCredentialsException("Invalid roll number or password");
    }

    public void logout(User user) {
        if (user instanceof Student) {
            NotificationCenter.getInstance().detach((Student) user);
        }
    }
}
