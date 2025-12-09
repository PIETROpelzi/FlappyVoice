@echo off
echo ========================================
echo   FIX COMPLETO MICROFONO
echo ========================================
echo.

cd /d "%~dp0"

echo [1/6] Chiusura Android Studio e Gradle...
taskkill /F /IM "studio64.exe" 2>nul
taskkill /F /IM java.exe 2>nul
timeout /t 2 /nobreak >nul

echo.
echo [2/6] Pulizia cache completa...
rmdir /S /Q ".gradle" 2>nul
rmdir /S /Q ".idea" 2>nul
rmdir /S /Q "app\build" 2>nul
rmdir /S /Q "build" 2>nul

echo.
echo [3/6] Verifica che il dispositivo sia connesso...
adb devices

echo.
echo [4/6] Disinstallazione app esistente...
adb uninstall com.example.flappyvoice 2>nul

echo.
echo [5/6] Riavvio ADB...
adb kill-server
timeout /t 2 /nobreak >nul
adb start-server
timeout /t 2 /nobreak >nul

echo.
echo [6/6] Pulizia completata!
echo.
echo ========================================
echo   PROSSIMI PASSI:
echo ========================================
echo 1. Apri Android Studio
echo 2. File -^> Invalidate Caches / Restart
echo 3. Aspetta che sincronizzi Gradle
echo 4. Build -^> Clean Project
echo 5. Build -^> Rebuild Project
echo 6. IMPORTANTE: Collega DISPOSITIVO FISICO (non emulatore!)
echo 7. Run e concedi il permesso microfono
echo 8. Fai RUMORE per testare!
echo ========================================
echo.
echo NOTA: L'emulatore potrebbe non supportare l'audio.
echo       Usa un telefono/tablet Android reale!
echo.
pause
