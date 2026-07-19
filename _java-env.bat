@echo off
rem ============================================================
rem Shared helper: make sure a JDK is usable in THIS window.
rem Called by run-server.bat and run-tests.bat - not run directly.
rem Order: 1) java already on PATH?  2) IntelliJ's downloaded JDKs
rem (%USERPROFILE%\.jdks - your ms-17 lives here)  3) common installs.
rem Changes nothing permanently - only this console session.
rem ============================================================

java -version >nul 2>&1
if not errorlevel 1 goto :done

set "FOUND="
for /d %%D in ("%USERPROFILE%\.jdks\*") do if exist "%%D\bin\java.exe" set "FOUND=%%D"
if not defined FOUND for /d %%D in ("%ProgramFiles%\Eclipse Adoptium\jdk*") do if exist "%%D\bin\java.exe" set "FOUND=%%D"
if not defined FOUND for /d %%D in ("%ProgramFiles%\Microsoft\jdk*") do if exist "%%D\bin\java.exe" set "FOUND=%%D"
if not defined FOUND for /d %%D in ("%ProgramFiles%\Java\jdk*") do if exist "%%D\bin\java.exe" set "FOUND=%%D"

if not defined FOUND (
  echo [ERROR] No Java found on this PC.
  echo Install one with:  winget install EclipseAdoptium.Temurin.21.JDK
  echo then re-run this script in a NEW window.
  exit /b 1
)

set "JAVA_HOME=%FOUND%"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo Using JAVA_HOME=%JAVA_HOME%

:done
java -version
exit /b 0
