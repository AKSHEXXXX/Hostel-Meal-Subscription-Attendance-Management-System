@echo off
echo ================================================
echo Compiling Hostel Meal Management System...
echo ================================================
echo.

if not exist "bin" mkdir bin

javac -d bin -sourcepath src src\com\hostelms\Main.java src\com\hostelms\model\*.java src\com\hostelms\service\*.java src\com\hostelms\dao\*.java src\com\hostelms\policy\*.java src\com\hostelms\observer\*.java src\com\hostelms\ui\*.java src\com\hostelms\util\*.java

if %errorlevel% == 0 (
    echo.
    echo ================================================
    echo Compilation successful!
    echo ================================================
    echo.
    echo Run the application using run.bat
) else (
    echo.
    echo ================================================
    echo Compilation failed!
    echo ================================================
)

pause
