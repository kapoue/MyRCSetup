@echo off
echo ========================================
echo    My RC Setup - Compilation APK
echo ========================================
echo.

REM Vérifier que nous sommes dans le bon dossier
if not exist "app\build.gradle.kts" (
    echo ERREUR: Ce script doit être exécuté depuis le dossier racine du projet MyRCSetup
    pause
    exit /b 1
)

echo 1. Nettoyage du projet...
call gradlew clean
if %ERRORLEVEL% neq 0 (
    echo ERREUR lors du nettoyage
    pause
    exit /b 1
)

echo.
echo 2. Compilation de l'APK de release...
call gradlew assembleRelease
if %ERRORLEVEL% neq 0 (
    echo ERREUR lors de la compilation
    pause
    exit /b 1
)

echo.
echo 3. Vérification de l'APK généré...
if exist "app\build\outputs\apk\release\app-release.apk" (
    echo ✅ APK généré avec succès !
    echo.
    echo 📁 Emplacement: app\build\outputs\apk\release\app-release.apk
    
    REM Obtenir la taille du fichier
    for %%A in ("app\build\outputs\apk\release\app-release.apk") do (
        set size=%%~zA
        set /a sizeMB=!size!/1024/1024
    )
    echo 📊 Taille: %sizeMB% MB
    
    echo.
    echo 🔧 Prochaines étapes:
    echo    1. Signer l'APK avec votre clé de signature
    echo    2. Tester l'installation sur un appareil
    echo    3. Distribuer à vos amis
    echo.
    echo 📖 Consultez DISTRIBUTION_APK.md pour plus de détails
    
) else (
    echo ❌ ERREUR: APK non trouvé
    exit /b 1
)

echo.
echo ========================================
echo           Compilation terminée !
echo ========================================
pause