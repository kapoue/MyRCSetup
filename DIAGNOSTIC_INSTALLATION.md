# 🔍 Diagnostic du problème d'installation APK

Le message "Un problème est survenu, Appli non installée" peut avoir plusieurs causes. Voici les solutions à essayer dans l'ordre :

## 1. **Sources inconnues non activées** (cause la plus fréquente)
- Allez dans **Paramètres** > **Sécurité** > **Sources inconnues**
- Activez l'option "Autoriser l'installation d'applications de sources inconnues"
- Ou sur Android récent : **Paramètres** > **Applications** > **Accès spécial** > **Installer des applications inconnues** > Sélectionnez votre gestionnaire de fichiers et activez

## 2. **APK non signé correctement** ⚠️ PROBLÈME PRINCIPAL
Le problème vient probablement du fait que l'APK n'a pas été signé avec `jarsigner`. Nous avons seulement copié le fichier unsigned. Il faut :
- Utiliser votre keystore `C:\Users\kapou\key-my-rc-setup.jks`
- Signer l'APK avec `jarsigner` avant installation

### Solution de signature manuelle :
```cmd
cd MyRCSetup
jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 -keystore "C:\Users\kapou\key-my-rc-setup.jks" "app\build\outputs\apk\release\app-release-unsigned.apk" my-rc-setup
```

Puis renommer le fichier signé :
```cmd
copy "app\build\outputs\apk\release\app-release-unsigned.apk" "MyRCSetup-v1.4.1-VRAIMENT-signed.apk"
```

## 3. **Version Android incompatible**
- L'app nécessite Android 7.0+ (API 24)
- Vérifiez la version Android de votre appareil dans **Paramètres** > **À propos du téléphone**

## 4. **Espace de stockage insuffisant**
- L'APK fait 10,8 MB, vérifiez l'espace libre
- Libérez de l'espace si nécessaire

## 5. **Ancienne version déjà installée**
- Si une version précédente existe, désinstallez-la d'abord
- Ou utilisez la même signature pour permettre la mise à jour

## 6. **Fichier APK corrompu**
- Retéléchargez l'APK
- Vérifiez que le transfert s'est bien passé

## 🎯 Diagnostic rapide
Pour vérifier si l'APK est correctement signé :
```cmd
jarsigner -verify -verbose -certs "MyRCSetup-v1.4.1-signed.apk"
```

Si le résultat contient "jar verified", l'APK est correctement signé.
Si non, il faut le signer avec jarsigner.

## 📱 Test alternatif
Essayez d'installer l'APK via ADB :
```cmd
adb install "MyRCSetup-v1.4.1-signed.apk"
```

Cela donnera un message d'erreur plus précis.