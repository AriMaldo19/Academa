
@echo off
setlocal
cd /d "%~dp0"

echo =====================================
echo ACADEMA - COMPILACION Y PRUEBAS
echo =====================================
echo.

where javac >nul 2>&1
if errorlevel 1 (
    echo ERROR: No se encontro el compilador Java.
    exit /b 1
)

where java >nul 2>&1
if errorlevel 1 (
    echo ERROR: No se encontro Java.
    exit /b 1
)

echo [1/2] Compilando clases...
if not exist "out\pruebas" mkdir "out\pruebas"

javac -encoding UTF-8 -d "out\pruebas" src\inicial\Gestor.java src\inicial\Resultado.java src\pruebas\PruebasInicial.java

if errorlevel 1 (
    echo ERROR: Fallo la compilacion.
    exit /b 1
)

echo.
echo [2/2] Ejecutando pruebas...
echo.

java -cp "out\pruebas" pruebas.PruebasInicial

if errorlevel 1 (
    echo.
    echo ERROR: Una o mas pruebas fallaron.
    exit /b 1
)

echo.
echo =====================================
echo PRUEBAS FINALIZADAS CORRECTAMENTE
echo =====================================

exit /b 0
