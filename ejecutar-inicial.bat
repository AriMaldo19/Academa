@echo off
cd /d "%~dp0"
if not exist out mkdir out
javac --add-modules jdk.httpserver -encoding UTF-8 -d out src\interfaz\*.java src\inicial\*.java src\refactorizado\*.java src\pruebas\*.java
if errorlevel 1 (
  echo No compilo. Revisar que java y javac esten instalados.
  pause
  exit /b 1
)
java --add-modules jdk.httpserver -cp out inicial.Main