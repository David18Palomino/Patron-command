@echo off
chcp 65001 > nul
echo =====================================================================
echo  Compilando el proyecto (Java 17+)...
echo =====================================================================
if not exist "bin" mkdir bin
javac -d bin -encoding UTF-8 src/com/building/automation/MainLauncher.java src/com/building/automation/before/*.java src/com/building/automation/before/device/*.java src/com/building/automation/before/panel/*.java src/com/building/automation/after/*.java src/com/building/automation/after/command/*.java src/com/building/automation/after/receiver/*.java src/com/building/automation/after/invoker/*.java src/com/building/automation/gui/*.java src/com/building/automation/gui/components/*.java

if %ERRORLEVEL% NEQ 0 (
    echo Error durante la compilacion.
    pause
    exit /b %ERRORLEVEL%
)

echo.
java -cp bin com.building.automation.MainLauncher
pause
