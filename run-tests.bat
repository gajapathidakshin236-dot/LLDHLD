@echo off
rem ============================================================
rem ONE-CLICK LOCAL TESTS - double-click to run the JUnit suite
rem (6 API tests + 1 rate-limit filter test + 4 strategy tests).
rem Needs no running server - tests boot the app internally.
rem Look for:  BUILD SUCCESS   and   Tests run: 11, Failures: 0
rem ============================================================
title api-server - JUnit tests
cd /d "%~dp0"

call _java-env.bat
if errorlevel 1 ( pause & exit /b 1 )

call mvnw.cmd test
pause
