@echo off
setlocal enabledelayedexpansion

REM ===========================================================
REM 🚀 Smart Build Script for Gradle Spring Boot Microservices
REM ===========================================================

set ROOT_DIR=%~dp0
set SERVICES_DIR=%ROOT_DIR%services

REM ==============================
REM 🔢 Define service list here
REM ==============================
set SERVICE_1=api-gateway-service
set SERVICE_2=zaplink-service-registry
set SERVICE_3=zaplink-shortner-service
set SERVICE_4=zaplink-processor-service
set SERVICE_5=zaplink-manager-service

REM ==============================
REM 🧾 Show service menu
REM ==============================
echo ===========================================================
echo Available Services to Build:
echo ===========================================================
echo [1] %SERVICE_1%
echo [2] %SERVICE_2%
echo [3] %SERVICE_3%
echo [4] %SERVICE_4%
echo [5] %SERVICE_5%
echo [all] Build ALL services
echo ===========================================================

set /p CHOICE="Enter service numbers (e.g., 1 3 5 or all): "
echo.

if /i "%CHOICE%"=="all" (
    set BUILD_LIST=1 2 3 4 5
) else (
    set BUILD_LIST=%CHOICE%
)

echo ===========================================================
echo 🧱 Starting Build Process...
echo ===========================================================

for %%N in (%BUILD_LIST%) do (
    call :build_service %%N
)

echo.
echo ===========================================================
echo ✅ Build Process Complete!
echo ===========================================================

pause
exit /b

REM ==============================
REM 🧩 Function to Build Service
REM ==============================
:build_service
set NUM=%1
set SERVICENAME=!SERVICE_%NUM%!

if "!SERVICENAME!"=="" (
    echo ⚠️ Invalid choice: %NUM%
    exit /b 1
)

echo -----------------------------------------------------------
echo 🚀 Building Service [!SERVICENAME!]
echo -----------------------------------------------------------

cd /d "%SERVICES_DIR%\!SERVICENAME!"

if exist "gradlew.bat" (
    call gradle clean build
) else (
    cd /d "%ROOT_DIR%"
    call ".\gradlew.bat" :!SERVICENAME!:clean :!SERVICENAME!:build
)

if errorlevel 1 (
    echo ❌ Build failed for !SERVICENAME!
    cd /d "%ROOT_DIR%"
    exit /b 1
)

echo ✅ Build succeeded for !SERVICENAME!
cd /d "%ROOT_DIR%"
exit /b 0
