@echo off
setlocal
echo ========================================================
echo  Online Grocery Order Management System
echo  Compiling and launching application on http://localhost:8080
echo ========================================================

where mvn >nul 2>nul
if %ERRORLEVEL% equ 0 (
    call mvn clean compile exec:java
    goto done
)

if exist "C:\Program Files\JetBrains\IntelliJ IDEA 2026.2.3\plugins\maven-plugin\lib\maven3\bin\mvn.cmd" (
    echo Using IntelliJ IDEA bundled Maven and JDK...
    set "JAVA_HOME=C:\Program Files\JetBrains\IntelliJ IDEA 2026.2.3\jbr"
    call "C:\Program Files\JetBrains\IntelliJ IDEA 2026.2.3\plugins\maven-plugin\lib\maven3\bin\mvn.cmd" clean compile exec:java
    goto done
)

echo [ERROR] Maven not found on PATH or in IntelliJ directory.
echo Please install Maven or add it to PATH.
pause
exit /b 1

:done
pause
