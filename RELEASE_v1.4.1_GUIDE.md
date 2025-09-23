# Guide de création de la Release GitHub v1.4.1

## 📋 Instructions pour créer la release manuellement

### 1. Aller sur GitHub
- Ouvrez votre navigateur et allez sur : https://github.com/kapouik/MyRCSetup
- Connectez-vous à votre compte GitHub

### 2. Créer une nouvelle release
- Cliquez sur l'onglet **"Releases"** (à droite de "Code")
- Cliquez sur **"Create a new release"**

### 3. Configurer la release
- **Tag version** : `v1.4.1`
- **Release title** : `My RC Setup v1.4.1 - Status Bar Fix`
- **Target** : `main` (branche principale)

### 4. Description de la release
Copiez-collez le texte suivant dans la description :

```markdown
## Corrections v1.4.1

### 🔧 Corrections
- **Barre de statut visible** : La barre de statut (heure, batterie, notifications) est maintenant correctement affichée
- Suppression de l'appel `enableEdgeToEdge()` qui masquait la barre de statut
- Ajout du padding approprié avec `Scaffold` pour l'interface système

### 📱 Installation
1. Téléchargez le fichier `MyRCSetup-v1.4.1-signed.apk`
2. Activez l'installation depuis des sources inconnues dans les paramètres Android
3. Installez l'APK sur votre appareil

### 🏁 Fonctionnalités
- Gestion complète des sessions RC
- Suivi des réglages de voiture
- Interface Material Design 3
- Stockage local sécurisé
- Navigation intuitive

---

## Fixes v1.4.1

### 🔧 Bug Fixes
- **Status bar visibility**: Status bar (time, battery, notifications) is now properly displayed
- Removed `enableEdgeToEdge()` call that was hiding the status bar
- Added proper padding with `Scaffold` for system UI

### 📱 Installation
1. Download `MyRCSetup-v1.4.1-signed.apk`
2. Enable installation from unknown sources in Android settings
3. Install the APK on your device
```

### 5. Attacher l'APK
- Dans la section **"Attach binaries"**, cliquez sur **"Choose files"**
- Sélectionnez le fichier `MyRCSetup-v1.4.1-signed.apk` depuis le dossier MyRCSetup
- Attendez que l'upload se termine

### 6. Publier la release
- Cochez **"Set as the latest release"**
- Cliquez sur **"Publish release"**

## ✅ Vérification
Après publication, la release sera disponible à l'adresse :
https://github.com/kapouik/MyRCSetup/releases/tag/v1.4.1

## 📱 Test de l'APK
1. Téléchargez l'APK depuis la release GitHub
2. Installez-le sur votre appareil Android
3. Vérifiez que la barre de statut est bien visible
4. Confirmez que la version affichée dans les paramètres Android est "1.4.1"

## 🎯 Distribution
Une fois testé, vous pouvez partager le lien de la release avec vos amis du club RC :
https://github.com/kapouik/MyRCSetup/releases/latest