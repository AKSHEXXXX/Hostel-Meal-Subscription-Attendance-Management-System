@echo off
echo ================================================
echo Running Hostel Meal Management System...
echo ================================================
echo.

if not exist "bin" (
    echo Error: Project not compiled. Run compile.bat first.
    pause
    exit /b
)

java -cp bin com.hostelms.Main

pause
