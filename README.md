# Hostel Meal Subscription & Attendance Management System

A Java desktop application for managing hostel meal subscriptions and attendance tracking.

## Project Overview

This system allows students to select meals in advance and enables hostel administrators to mark attendance and automatically process charges and refunds. The application demonstrates comprehensive Object-Oriented Programming concepts including inheritance, interfaces, polymorphism, design patterns, and file-based persistence.

## Features

### Student Features
- **Login System**: Secure authentication with roll number and password
- **Meal Selection**: Select or cancel breakfast, lunch, and dinner for the next day anytime
- **Balance Management**: View current balance in real-time
- **Transaction History**: Track all charges, refunds, and recharges
- **Notifications**: Receive real-time notifications for all transactions

### Admin Features
- **Configuration Management**: Set meal prices and refund policies
- **Attendance Marking**: View meal selections and mark student attendance
- **Automatic Processing**: Charges and refunds are automatically calculated and applied
- **Reporting**: Generate daily reports with meal counts and financial summaries
- **Refund Policies**: Choose between Full (100%), 80%, or 50% refund policies

## Design Patterns Implemented

1. **Observer Pattern**: NotificationCenter (Singleton) notifies students of transactions in real-time
2. **Strategy Pattern**: Pluggable refund policies (FullRefundPolicy, EightyPercentRefundPolicy, FiftyPercentRefundPolicy)
3. **Singleton Pattern**: NotificationCenter ensures single instance for managing notifications
4. **MVC Pattern**: Separation of Model (data), View (UI), and Controller (Services)

## OOP Concepts Demonstrated

- **Inheritance**: User (abstract) → Student, Admin
- **Interfaces**: Observer, Subject, RefundPolicy
- **Polymorphism**: Multiple refund policy implementations, User types
- **Encapsulation**: Private fields with getters/setters
- **Abstraction**: Abstract User class, interfaces for patterns
- **Collections**: ArrayList, HashMap for managing data
- **Exception Handling**: Custom exceptions (InvalidCredentialsException, MealSelectionException, etc.)

## Project Structure

```
OOPS Project/
├── src/
│   └── com/hostelms/
│       ├── Main.java                    - Application entry point
│       ├── model/                       - Data models
│       │   ├── User.java (abstract)
│       │   ├── Student.java
│       │   ├── Admin.java
│       │   ├── MealRecord.java
│       │   ├── Transaction.java
│       │   ├── Notification.java
│       │   ├── MealConfiguration.java
│       │   ├── MealType.java (enum)
│       │   └── TransactionType.java (enum)
│       ├── service/                     - Business logic
│       │   ├── AuthenticationService.java
│       │   ├── MealService.java
│       │   ├── TransactionService.java
│       │   ├── AttendanceService.java
│       │   ├── NotificationService.java
│       │   └── ReportService.java
│       ├── dao/                         - Data access objects
│       │   ├── StudentDAO.java
│       │   ├── MealRecordDAO.java
│       │   ├── TransactionDAO.java
│       │   ├── AttendanceDAO.java
│       │   └── ConfigurationDAO.java
│       ├── policy/                      - Strategy pattern
│       │   ├── RefundPolicy.java (interface)
│       │   ├── FullRefundPolicy.java
│       │   ├── EightyPercentRefundPolicy.java
│       │   └── FiftyPercentRefundPolicy.java
│       ├── observer/                    - Observer pattern
│       │   ├── Observer.java (interface)
│       │   ├── Subject.java (interface)
│       │   └── NotificationCenter.java (Singleton)
│       ├── ui/                          - Swing GUI
│       │   ├── LoginFrame.java
│       │   ├── StudentDashboard.java
│       │   ├── MealSelectionPanel.java
│       │   ├── TransactionHistoryPanel.java
│       │   ├── NotificationPanel.java
│       │   ├── AdminDashboard.java
│       │   ├── ConfigurationPanel.java
│       │   ├── AttendancePanel.java
│       │   └── ReportPanel.java
│       └── util/                        - Utilities and exceptions
│           ├── Constants.java
│           ├── DateTimeUtil.java
│           ├── ValidationUtil.java
│           └── (7 custom exceptions)
├── data/                                - File-based storage
│   ├── students.txt
│   ├── meals.txt
│   ├── attendance.txt
│   ├── transactions.txt
│   └── config.txt
├── bin/                                 - Compiled classes
├── compile.bat                          - Compilation script
├── run.bat                              - Execution script
└── README.md                            - This file
```

## Requirements

- **Java**: JDK 21 or higher
- **OS**: Windows (for .bat scripts)
- **IDE**: VS Code (or any Java IDE)

## Setup & Installation

1. **Clone or download** the project to your local machine

2. **Ensure Java is installed**:
   ```
   java -version
   ```

3. **Navigate to project directory**:
   ```
   cd "OOPS Project"
   ```

## Compilation

Run the compile script:
```
compile.bat
```

This will compile all Java files and place the .class files in the `bin/` directory.

## Running the Application

Run the application:
```
run.bat
```

This will automatically launch **TWO login windows side by side**:
- **Left window**: Student Login
- **Right window**: Admin Login

This dual-window setup enables easy demonstration of real-time synchronization between student and admin panels.

## Default Credentials

### Admin Account
- **Roll Number**: ADMIN
- **Password**: admin123

### Sample Student Accounts
- **Roll Number**: 2021A7PS0001H, **Password**: pass123, **Balance**: Rs. 1500.00
- **Roll Number**: 2021A7PS0002H, **Password**: pass456, **Balance**: Rs. 2000.00
- **Roll Number**: 2021A7PS0003H, **Password**: pass789, **Balance**: Rs. 1000.00
- **Roll Number**: 2021A7PS0004H, **Password**: pass321, **Balance**: Rs. 1800.00
- **Roll Number**: 2021A7PS0005H, **Password**: pass654, **Balance**: Rs. 1200.00

## Usage Guide

### Quick Start for Demonstration

When you run `run.bat`, two login windows open automatically:

**Left Window - Student Login:**
```
Roll Number: 2021A7PS0001H (or any student)
Password: pass123
```

**Right Window - Admin Login:**
```
Roll Number: ADMIN
Password: admin123
```

Login to both simultaneously to see real-time synchronization!

### For Students

1. **Login**: Enter your roll number and password in the Student Login window
2. **Select Meals**:
   - Navigate to "Meal Selection" tab
   - Check boxes for breakfast, lunch, or dinner for **tomorrow**
   - Click "Update Selection" anytime (cutoff time removed for demonstration)
   - Notification appears instantly via Observer pattern
3. **View Balance**: Displayed at the top of the dashboard
4. **Transaction History**:
   - View all charges and refunds in the "Transaction History" tab
   - Click "Refresh" button to load latest transactions from file
   - Balance updates persist to file; logout/login to see updated balance in UI
5. **Notifications**:
   - Check "Notifications" tab for real-time updates
   - Updates appear instantly without refresh (Observer pattern)
6. **Logout**: Click "Logout" to return to login screen

### For Administrators

1. **Login**: Use ADMIN credentials in the Admin Login window
2. **Configure System** (Configuration Tab):
   - Set breakfast, lunch, and dinner prices
   - Choose refund policy (Full/80%/50%)
   - Click "Save Configuration"
3. **Mark Attendance** (Mark Attendance Tab):
   - **Important**: Select **"Tomorrow"** from date dropdown (matches student selections)
   - Click "Load Meal Selections"
   - Review student selections in table
   - Mark attendance dropdowns (PRESENT/ABSENT)
   - Click "Save Attendance & Process Charges"
   - System automatically:
     * Charges students marked PRESENT for selected meals
     * Refunds students marked ABSENT based on refund policy
     * Sends notifications to all logged-in students instantly
4. **Generate Reports** (Reports Tab):
   - Select date (Today, Yesterday, or previous days)
   - Click "Generate Report"
   - View meal counts and financial summary
5. **Transaction History** (Admin View):
   - View all students' transactions system-wide
   - Click "Refresh" to load latest data

## File Formats

All data is stored in comma-delimited text files in the `data/` directory:

### students.txt
```
rollNumber,name,password,balance
2021A7PS0001H,Rahul Kumar,pass123,1500.00
```

### meals.txt
```
rollNumber,date,breakfast,lunch,dinner,status
2021A7PS0001H,2025-11-29,true,true,false,PENDING
```

### attendance.txt
```
rollNumber,date,breakfast,lunch,dinner,timestamp
2021A7PS0001H,2025-11-28,PRESENT,PRESENT,ABSENT,2025-11-28T19:30:00
```

### transactions.txt
```
txnId,rollNumber,date,type,amount,mealType,description,timestamp
TXN001,2021A7PS0001H,2025-11-28,CHARGE,30.00,BREAKFAST,Charge for BREAKFAST,2025-11-28T19:30:00
```

### config.txt
```
key,value
breakfastPrice,30.00
lunchPrice,50.00
dinnerPrice,40.00
refundPolicy,EIGHTY_PERCENT
mealCutoffTime,20:00
```

## How It Works

### Meal Selection Flow
1. Student selects meals for tomorrow anytime
2. MealService validates date only
3. MealRecord is saved to meals.txt
4. NotificationCenter sends confirmation to student

### Attendance & Charging Flow
1. Admin loads meal selections for a date
2. Admin marks attendance (PRESENT/ABSENT)
3. For each student/meal:
   - If selected + present → charge meal price
   - If selected + absent → refund based on policy
   - Update student balance
   - Save transaction
   - Notify student via Observer pattern
4. MealRecord status updated to COMPLETED

### Observer Pattern Flow
1. Student logs in → attached to NotificationCenter
2. Any service creates Notification
3. NotificationCenter.notifyStudent() called
4. Student.update() adds notification to list
5. UI displays in NotificationPanel
6. Student logs out → detached from NotificationCenter

### Strategy Pattern Flow
1. Admin sets refund policy in Configuration
2. AttendanceService loads policy name from config
3. Factory method creates appropriate RefundPolicy object
4. calculateRefund() called for each missed meal
5. Different refund amount based on policy

## Exception Handling

The system implements custom exceptions:
- **InvalidCredentialsException**: Login failures
- **MealSelectionException**: Invalid meal operations (wrong date)
- **InsufficientBalanceException**: Balance too low for charges
- **DataAccessException**: File I/O errors
- **InvalidDateException**: Extends MealSelectionException for date validation
- **FileCorruptionException**: Corrupted data file entries

## Key Features Implemented

- **Real-time Balance Updates**: Balance displayed and updated instantly
- **Automatic Charge/Refund**: No manual calculation needed
- **Flexible Refund Policies**: Admin can choose policy that suits hostel
- **Date Validation**: Can only select meals for tomorrow
- **No Time Restrictions**: Cutoff time removed for easy demonstration to professors
- **Transaction Audit Trail**: Complete history of all financial operations
- **Observer Notifications**: Students instantly notified of charges/refunds

## Technical Highlights

- **Java 21 Features**: Switch expressions, modern APIs
- **Swing GUI**: Clean, simple layouts (BorderLayout, GridLayout, FlowLayout)
- **File-based Persistence**: BufferedReader/Writer with error handling
- **Design Patterns**: Observer, Strategy, Singleton
- **Exception Hierarchy**: Custom exceptions with inheritance
- **Collections Framework**: ArrayList for lists, HashMap for maps
- **Modern Date/Time**: LocalDate, LocalDateTime, LocalTime

## Project Accomplishments

This project successfully demonstrates:
1. ✅ **OOP Principles**: Inheritance, interfaces, polymorphism, encapsulation
2. ✅ **Design Patterns**: Observer, Strategy, Singleton
3. ✅ **GUI Development**: Swing with multiple frames and panels
4. ✅ **File I/O**: Reading, writing, updating text files
5. ✅ **Exception Handling**: Custom exception hierarchy
6. ✅ **Collections**: ArrayList, HashMap usage
7. ✅ **Layered Architecture**: Model-DAO-Service-UI separation
8. ✅ **Real-world Problem**: Practical hostel management solution

## Demonstration Guide

### Setup (30 seconds)
1. Run `run.bat`
2. Two login windows appear side by side automatically
3. Login to **both windows** simultaneously:
   - Left: Student (2021A7PS0001H / pass123)
   - Right: Admin (ADMIN / admin123)

### Live Demo Flow (5-7 minutes)

**Step 1: Student Selects Meals**
- In Student window: Go to "Meal Selection" tab
- Select ☑ Breakfast, ☑ Lunch, ☑ Dinner for tomorrow
- Click "Update Selection"
- **Instant notification appears** (Observer pattern!)

**Step 2: Admin Processes Attendance**
- In Admin window: Go to "Mark Attendance" tab
- Select **"Tomorrow"** from dropdown (critical!)
- Click "Load Meal Selections"
- Table shows student's selections
- Mark PRESENT for all meals
- Click "Save Attendance & Process Charges"

**Step 3: Watch Real-Time Sync! ✨**
- Switch to Student window
- Click "Notifications" tab
- **Three charge notifications appear instantly!** (Observer pattern in action)
- Click "Transaction History" → Click "Refresh"
- **Three new transactions visible!**
- Balance updated in database (logout/login to see UI update)

### Demonstrating Strategy Pattern

**For Refunds:**
- Student selects meals for tomorrow
- Admin marks student **ABSENT**
- System applies configured refund policy (50% by default)
- Student receives refund notification instantly
- Transaction history shows REFUND entry
- Admin can change policy to 80% or 100% in Configuration tab

### Key Demonstration Points

**Observer Pattern:**
- NotificationCenter (Singleton) broadcasts to all logged-in students
- Real-time updates without manual refresh
- Loose coupling between admin actions and student notifications

**Strategy Pattern:**
- Three RefundPolicy implementations (Full/80%/50%)
- Runtime policy selection via configuration
- Different refund amounts based on selected policy

**File Persistence:**
- All changes saved to text files (students.txt, transactions.txt, meals.txt)
- Complete transaction audit trail
- Click "Refresh" buttons to load latest data from files

**OOP Principles:**
- Inheritance: User → Student, Admin
- Polymorphism: Multiple RefundPolicy implementations
- Encapsulation: Private fields with getters/setters
- Abstraction: Abstract User class, interfaces

### Important Notes

- **Dual Login Windows**: Demonstrates real-time synchronization
- **Tomorrow's Date**: Students select meals for tomorrow only
- **Refresh Buttons**: Load latest data from file system
- **Balance Updates**: Persisted to file; logout/login to see in UI
- **Real-time Notifications**: Work instantly via Observer pattern
- **No Time Restrictions**: Cutoff time removed for demonstration flexibility

## Future Enhancements

Potential improvements (beyond current scope):
- Re-implement cutoff time validation for production use
- Database integration (SQLite/MySQL)
- Email/SMS notifications
- QR code-based attendance
- Export reports to PDF/Excel
- Password encryption (BCrypt)
- Multi-language support
- Web-based interface
- Mobile app
- Analytics dashboard
- Backup/restore functionality

## Authors

**Harsh** (2025)
CS F213 Object-Oriented Programming
BITS Pilani, Dubai Campus

## Course Information

- **Course**: CS F213 Object-Oriented Programming
- **Institution**: BITS Pilani, Dubai Campus
- **Academic Year**: 2024-2025

## License

Academic project - not for commercial use

## Acknowledgments

This project was developed as part of the CS F213 course requirements, demonstrating comprehensive understanding of Object-Oriented Programming concepts, design patterns, and software engineering principles.

---

For any questions or issues, please refer to the code comments or contact the project author.
