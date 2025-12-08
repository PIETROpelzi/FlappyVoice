@echo off
echo ========================================
echo   PULIZIA FORZATA PROGETTO
echo ========================================
echo.

cd /d "%~dp0"

echo Chiusura eventuali processi Gradle...
taskkill /F /IM java.exe 2>nul
timeout /t 2 /nobreak >nul

echo.
echo Eliminazione file Kotlin...

REM Forza eliminazione con attributi
attrib -R -S -H "app\src\main\java\com\example\flappyvoice\MainActivity.kt" 2>nul
del /F /Q "app\src\main\java\com\example\flappyvoice\MainActivity.kt" 2>nul

attrib -R -S -H "app\src\main\java\com\example\flappyvoice\ui\*.*" /S 2>nul
rmdir /S /Q "app\src\main\java\com\example\flappyvoice\ui" 2>nul

attrib -R -S -H "app\src\androidTest\java\com\example\flappyvoice\ExampleInstrumentedTest.kt" 2>nul
del /F /Q "app\src\androidTest\java\com\example\flappyvoice\ExampleInstrumentedTest.kt" 2>nul

attrib -R -S -H "app\src\test\java\com\example\flappyvoice\ExampleUnitTest.kt" 2>nul
del /F /Q "app\src\test\java\com\example\flappyvoice\ExampleUnitTest.kt" 2>nul

echo.
echo Pulizia cache profonda...
rmdir /S /Q ".gradle" 2>nul
rmdir /S /Q ".idea" 2>nul
rmdir /S /Q "app\build" 2>nul
rmdir /S /Q "build" 2>nul

echo.
echo ========================================
echo   COMPLETATO!
echo ========================================
echo.
echo Ora:
echo 1. Apri Android Studio
echo 2. File -^> Invalidate Caches
echo 3. Riavvia
echo 4. Sync Gradle
echo 5. Rebuild
echo.
pause
