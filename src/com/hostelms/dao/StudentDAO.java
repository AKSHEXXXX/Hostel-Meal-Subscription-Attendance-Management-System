package com.hostelms.dao;

import com.hostelms.model.Admin;
import com.hostelms.model.Student;
import com.hostelms.util.Constants;
import com.hostelms.util.DataAccessException;
import com.hostelms.util.FileCorruptionException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private static final String FILE_PATH = Constants.STUDENTS_FILE;

    public List<Student> loadAllStudents() throws DataAccessException {
        List<Student> students = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return students; // Empty list, file will be created on first save
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines

                try {
                    // Skip admin account when loading students
                    if (!line.startsWith(Constants.ADMIN_ROLL_NUMBER + ",")) {
                        Student student = parseStudent(line);
                        students.add(student);
                    }
                } catch (FileCorruptionException e) {
                    System.err.println("Skipping corrupted line: " + line);
                    // Continue processing other lines
                }
            }
        } catch (IOException e) {
            throw new DataAccessException("Error reading students file", e);
        }

        return students;
    }

    public Student findByRollNumber(String rollNumber) throws DataAccessException {
        List<Student> students = loadAllStudents();
        for (Student student : students) {
            if (student.getRollNumber().equals(rollNumber)) {
                return student;
            }
        }
        return null;
    }

    public void saveStudent(Student student) throws DataAccessException {
        List<Student> students = loadAllStudents();
        students.add(student);
        writeAllStudents(students);
    }

    public void updateStudent(Student student) throws DataAccessException {
        List<Student> students = loadAllStudents();

        boolean found = false;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getRollNumber().equals(student.getRollNumber())) {
                students.set(i, student);
                found = true;
                break;
            }
        }

        if (!found) {
            students.add(student); // Add if new
        }

        writeAllStudents(students);
    }

    public Admin loadAdmin() throws DataAccessException {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            // Create default admin
            Admin defaultAdmin = new Admin(Constants.ADMIN_ROLL_NUMBER,
                Constants.ADMIN_DEFAULT_NAME, Constants.ADMIN_DEFAULT_PASSWORD);
            saveAdmin(defaultAdmin);
            return defaultAdmin;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                if (line.startsWith(Constants.ADMIN_ROLL_NUMBER + ",")) {
                    return parseAdmin(line);
                }
            }
        } catch (IOException e) {
            throw new DataAccessException("Error reading admin account", e);
        }

        // Create default admin if not found
        Admin defaultAdmin = new Admin(Constants.ADMIN_ROLL_NUMBER,
            Constants.ADMIN_DEFAULT_NAME, Constants.ADMIN_DEFAULT_PASSWORD);
        saveAdmin(defaultAdmin);
        return defaultAdmin;
    }

    public void saveAdmin(Admin admin) throws DataAccessException {
        File file = new File(FILE_PATH);
        List<String> lines = new ArrayList<>();

        // Read all existing lines
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith(Constants.ADMIN_ROLL_NUMBER + ",")) {
                        lines.add(line);
                    }
                }
            } catch (IOException e) {
                throw new DataAccessException("Error reading students file", e);
            }
        }

        // Write admin line at the end
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            writer.write(adminToString(admin));
            writer.newLine();
        } catch (IOException e) {
            throw new DataAccessException("Error writing admin account", e);
        }
    }

    // Helper methods
    private Student parseStudent(String line) throws FileCorruptionException {
        String[] parts = line.split(",");
        if (parts.length != 4) {
            throw new FileCorruptionException("Invalid student data format");
        }

        try {
            String rollNumber = parts[0].trim();
            String name = parts[1].trim();
            String password = parts[2].trim();
            double balance = Double.parseDouble(parts[3].trim());

            return new Student(rollNumber, name, password, balance);
        } catch (NumberFormatException e) {
            throw new FileCorruptionException("Invalid balance format");
        }
    }

    private Admin parseAdmin(String line) throws FileCorruptionException {
        String[] parts = line.split(",");
        if (parts.length != 4) {
            throw new FileCorruptionException("Invalid admin data format");
        }

        String rollNumber = parts[0].trim();
        String name = parts[1].trim();
        String password = parts[2].trim();

        return new Admin(rollNumber, name, password);
    }

    private String studentToString(Student student) {
        return String.format("%s,%s,%s,%.2f",
            student.getRollNumber(),
            student.getName(),
            student.getPassword(),
            student.getBalance());
    }

    private String adminToString(Admin admin) {
        return String.format("%s,%s,%s,0.00",
            admin.getRollNumber(),
            admin.getName(),
            admin.getPassword());
    }

    private void writeAllStudents(List<Student> students) throws DataAccessException {
        // Load admin first
        Admin admin = null;
        try {
            admin = loadAdmin();
        } catch (DataAccessException e) {
            // Ignore if admin doesn't exist yet
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Student student : students) {
                writer.write(studentToString(student));
                writer.newLine();
            }

            // Write admin at the end
            if (admin != null) {
                writer.write(adminToString(admin));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DataAccessException("Error writing students file", e);
        }
    }
}
