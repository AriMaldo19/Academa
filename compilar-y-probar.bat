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

echo [1/3] Compilando clases de ambas versiones y pruebas...
if not exist "out\pruebas" mkdir "out\pruebas"
javac -encoding UTF-8 -d "out\pruebas" src\interfaz\Servidor.java src\inicial\*.java src\refactorizado\*.java src\pruebas\*.java
if errorlevel 1 (
    echo ERROR: Fallo la compilacion.
    exit /b 1
)

echo.
echo [2/3] Ejecutando pruebas de la version inicial...
echo.
java -cp "out\pruebas" pruebas.PruebasInicial
if errorlevel 1 (
    echo ERROR: Fallaron pruebas iniciales.
    exit /b 1
)

echo.
echo [3/3] Ejecutando comparacion entre versiones...
echo.
java -cp "out\pruebas" pruebas.Comparacion
if errorlevel 1 (
    echo ERROR: Fallaron pruebas comparativas.
    exit /b 1
)

echo.
echo =====================================
echo TODAS LAS PRUEBAS FINALIZARON CORRECTAMENTE
echo =====================================
exit /b 0
