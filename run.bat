@echo off
REM ──────────────────────────────────────────────────────────────────────────
REM  Student Management System – Build & Run (Windows)
REM ──────────────────────────────────────────────────────────────────────────

set LIB=lib
set OUT=out
set SRC=src
set MAIN=com.sms.Main
set JAR=%LIB%\sqlite-jdbc-3.45.0.0.jar
set SLF4J_JAR=%LIB%\slf4j-api-2.0.16.jar

echo === Student Management System ===

REM 1. Check Java
where java >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java not found. Install JDK 11+ and add to PATH.
    pause & exit /b 1
)

REM 2. Check required JARs
if not exist "%JAR%" (
    echo.
    echo SQLite JDBC JAR not found at %JAR%
    echo Please download it from:
    echo   https://github.com/xerial/sqlite-jdbc/releases/download/3.45.0.0/sqlite-jdbc-3.45.0.0.jar
    echo and place it in the lib\ folder, then re-run this script.
    echo.
    pause & exit /b 1
)

if not exist "%SLF4J_JAR%" (
    echo.
    echo SLF4J API JAR not found at %SLF4J_JAR%
    echo Please download it from:
    echo   https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.16/slf4j-api-2.0.16.jar
    echo and place it in the lib\ folder, then re-run this script.
    echo.
    pause & exit /b 1
)

REM 3. Compile
echo Compiling...
if not exist %OUT% mkdir %OUT%
dir /s /b %SRC%\*.java > sources.txt
javac -cp "%JAR%;%SLF4J_JAR%" -d %OUT% @sources.txt
if errorlevel 1 (
    echo ERROR: Compilation failed.
    del sources.txt
    pause & exit /b 1
)
del sources.txt
echo Compilation successful.

REM 4. Run
echo Starting application...
echo.
java -cp "%OUT%;%JAR%;%SLF4J_JAR%" %MAIN%
pause
