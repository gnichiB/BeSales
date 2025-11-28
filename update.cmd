@echo off
setlocal

echo ====================================
echo    MISE A JOUR GIT - AUTH TOKEN
echo ====================================

cd /d "C:\BecSales"

:: Configuration (à personnaliser)
set GITHUB_USERNAME=gnichiB
set GITHUB_TOKEN=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

echo.
echo 1. Ajout des fichiers...
git add .

echo.
echo 2. Creation du commit...
git commit -m "Update: %date% %time%"

echo.
echo 3. Authentification avec token...
git push https://%GITHUB_USERNAME%:%GITHUB_TOKEN%@github.com/gnichiB/BeSales.git main

echo.
echo ====================================
echo    MISES A JOUR TERMINEE !
echo ====================================
echo.

:: Nettoyage sécurisé
set GITHUB_USERNAME=
set GITHUB_TOKEN=

pause
endlocal