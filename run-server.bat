@echo off
rem ============================================================
rem ONE-CLICK SERVER START - just double-click this file.
rem Finds Java automatically, then starts Spring Boot on
rem http://localhost:8080  (Maven itself is auto-downloaded by
rem mvnw.cmd - you never install Maven).
rem First run downloads libraries (~200 MB): be patient.
rem Stop the server: Ctrl+C in this window (or close it).
rem ============================================================
title api-server @ localhost:8080
cd /d "%~dp0"

call _java-env.bat
if errorlevel 1 ( pause & exit /b 1 )

echo.
echo Starting server ... success line to wait for:
echo   "Tomcat started on port 8080"
echo.
call mvnw.cmd spring-boot:run
echo.
echo Server stopped.
pause
