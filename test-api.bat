@echo off
rem ============================================================
rem ONE-CLICK API TEST DRIVE - run while the server is running
rem (start run-server.bat first, wait for "Tomcat started").
rem Exercises every endpoint with Windows' built-in curl.
rem ============================================================
title api-server - endpoint test drive
echo ================= GET /api/ping =================
curl.exe -s http://localhost:8080/api/ping
echo.
echo ================= GET /api/tasks (list) =================
curl.exe -s http://localhost:8080/api/tasks
echo.
echo ================= POST /api/tasks (create) =================
curl.exe -s -X POST http://localhost:8080/api/tasks -H "Content-Type: application/json" -d "{\"title\":\"Created by test-api.bat\",\"description\":\"live from my own machine\"}"
echo.
echo ================= PUT /api/tasks/1 (update) =================
curl.exe -s -X PUT http://localhost:8080/api/tasks/1 -H "Content-Type: application/json" -d "{\"title\":\"Updated by test-api.bat\",\"description\":\"x\",\"completed\":true}"
echo.
echo ================= DELETE /api/tasks/2 (expect HTTP 204) =================
curl.exe -s -i -X DELETE http://localhost:8080/api/tasks/2
echo ================= GET /api/tasks/2 (expect 404 error JSON) =================
curl.exe -s http://localhost:8080/api/tasks/2
echo.
echo ================= POST blank title (expect 400 + fieldErrors) =================
curl.exe -s -X POST http://localhost:8080/api/tasks -H "Content-Type: application/json" -d "{\"title\":\"\"}"
echo.
echo ================= RATE LIMIT: 10 rapid pings (expect 200s then 429s) =================
for /L %%i in (1,1,10) do curl.exe -s -o NUL -w "%%{http_code}\n" http://localhost:8080/api/ping
echo.
echo ================= Done - final task list =================
curl.exe -s http://localhost:8080/api/tasks
echo.
pause
