@echo off
echo ========================================
echo   PULIZIA PROGETTO FLAPPY VOICE
echo ========================================
echo.

cd /d "%~dp0"

echo [1/5] Eliminazione MainActivity.kt...
if exist "app\src\main\java\com\example\flappyvoice\MainActivity.kt" (
    del /F /Q "app\src\main\java\com\example\flappyvoice\MainActivity.kt"
    echo    - MainActivity.kt eliminato
) else (
    echo    - MainActivity.kt non trovato
)

echo.
echo [2/5] Eliminazione cartella ui con file Kotlin...
if exist "app\src\main\java\com\example\flappyvoice\ui" (
    rmdir /S /Q "app\src\main\java\com\example\flappyvoice\ui"
    echo    - Cartella ui eliminata
) else (
    echo    - Cartella ui non trovata
)

echo.
echo [3/5] Eliminazione test Kotlin...
if exist "app\src\androidTest\java\com\example\flappyvoice\ExampleInstrumentedTest.kt" (
    del /F /Q "app\src\androidTest\java\com\example\flappyvoice\ExampleInstrumentedTest.kt"
    echo    - ExampleInstrumentedTest.kt eliminato
) else (
    echo    - ExampleInstrumentedTest.kt non trovato
)

if exist "app\src\test\java\com\example\flappyvoice\ExampleUnitTest.kt" (
    del /F /Q "app\src\test\java\com\example\flappyvoice\ExampleUnitTest.kt"
    echo    - ExampleUnitTest.kt eliminato
) else (
    echo    - ExampleUnitTest.kt non trovato
)

echo.
echo [4/5] Pulizia cache Gradle...
if exist ".gradle" (
    rmdir /S /Q ".gradle"
    echo    - Cache Gradle eliminata
)

if exist "app\build" (
    rmdir /S /Q "app\build"
    echo    - Build folder eliminata
)

echo.
echo [5/5] Pulizia completata!
echo.
echo ========================================
echo   PROSSIMI PASSI:
echo ========================================
echo 1. Apri Android Studio
echo 2. File -^> Invalidate Caches / Restart
echo 3. File -^> Sync Project with Gradle Files
echo 4. Build -^> Clean Project
echo 5. Build -^> Rebuild Project
echo 6. Collega un dispositivo Android e premi Run!
echo ========================================
echo.
pause
