#!/bin/bash

echo "========================================"
echo "   My RC Setup - Compilation APK"
echo "========================================"
echo

# Vérifier que nous sommes dans le bon dossier
if [ ! -f "app/build.gradle.kts" ]; then
    echo "ERREUR: Ce script doit être exécuté depuis le dossier racine du projet MyRCSetup"
    exit 1
fi

echo "1. Nettoyage du projet..."
./gradlew clean
if [ $? -ne 0 ]; then
    echo "ERREUR lors du nettoyage"
    exit 1
fi

echo
echo "2. Compilation de l'APK de release..."
./gradlew assembleRelease
if [ $? -ne 0 ]; then
    echo "ERREUR lors de la compilation"
    exit 1
fi

echo
echo "3. Vérification de l'APK généré..."
if [ -f "app/build/outputs/apk/release/app-release.apk" ]; then
    echo "✅ APK généré avec succès !"
    echo
    echo "📁 Emplacement: app/build/outputs/apk/release/app-release.apk"
    
    # Obtenir la taille du fichier
    size=$(stat -f%z "app/build/outputs/apk/release/app-release.apk" 2>/dev/null || stat -c%s "app/build/outputs/apk/release/app-release.apk" 2>/dev/null)
    sizeMB=$((size / 1024 / 1024))
    echo "📊 Taille: ${sizeMB} MB"
    
    echo
    echo "🔧 Prochaines étapes:"
    echo "   1. Signer l'APK avec votre clé de signature"
    echo "   2. Tester l'installation sur un appareil"
    echo "   3. Distribuer à vos amis"
    echo
    echo "📖 Consultez DISTRIBUTION_APK.md pour plus de détails"
    
else
    echo "❌ ERREUR: APK non trouvé"
    exit 1
fi

echo
echo "========================================"
echo "          Compilation terminée !"
echo "========================================"