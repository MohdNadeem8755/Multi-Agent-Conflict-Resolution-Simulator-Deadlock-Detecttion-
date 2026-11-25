@echo off
cd /d %~dp0
where java >nul 2>&1
if errorlevel 1 (
  echo Java not found in PATH. Install Java and add to PATH.
  pause
  exit /b 1
)

rem If port 8080 in use, assume backend already running and keep script sleeping
netstat -ano | findstr :8080 >nul 2>&1
if %errorlevel%==0 (
  echo Port 8080 is already in use. Assuming backend is running. Sleeping...
  :WAITLOOP
  ping -n 61 127.0.0.1 >nul
  goto WAITLOOP
)

:loop
echo Starting Multi-Agent backend...
java -cp "%~dp0bin" app.Main
echo Backend exited. Restarting in 2 seconds...
ping -n 3 127.0.0.1 >nul
goto loop
