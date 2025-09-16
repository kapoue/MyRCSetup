# Instructions de Compilation - My RC Setup

## Prérequis

### Logiciels Requis
1. **Android Studio** (version Hedgehog 2023.1.1 ou plus récente)
   - Télécharger depuis : https://developer.android.com/studio
   - Installer avec les composants SDK par défaut

2. **Java Development Kit (JDK)**
   - JDK 17 ou plus récent
   - Inclus avec Android Studio ou téléchargeable séparément

3. **Git** (optionnel, pour le contrôle de version)
   - Télécharger depuis : https://git-scm.com/

### Configuration Android SDK
- **SDK minimum** : API 24 (Android 7.0)
- **SDK cible** : API 34 (Android 14)
- **Build Tools** : 34.0.0 ou plus récent

## Installation et Configuration

### 1. Préparation de l'Environnement

#### Ouvrir le Projet
1. Lancez Android Studio
2. Sélectionnez "Open an Existing Project"
3. Naviguez vers le dossier `MyRCSetup`
4. Cliquez sur "OK"

#### Synchronisation Gradle
1. Android Studio détectera automatiquement le projet Gradle
2. Cliquez sur "Sync Now" si demandé
3. Attendez la fin de la synchronisation (peut prendre quelques minutes)

### 2. Configuration des Dépendances

Les dépendances sont automatiquement gérées par Gradle :
- Jetpack Compose BOM 2023.10.01
- Room Database 2.6.1
- Navigation Compose 2.7.5
- Kotlin DateTime 0.5.0
- Material Design 3

### 3. Compilation

#### Via Android Studio (Recommandé)
1. **Build du projet** :
   - Menu : `Build` → `Make Project`
   - Raccourci : `Ctrl+F9` (Windows/Linux) ou `Cmd+F9` (Mac)

2. **Génération de l'APK** :
   - Menu : `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`
   - L'APK sera généré dans : `app/build/outputs/apk/debug/`

3. **APK de Release** :
   - Menu : `Build` → `Generate Signed Bundle / APK`
   - Suivez l'assistant pour créer/utiliser un keystore
   - Sélectionnez "APK" et configurez la signature

#### Via Ligne de Commande
```bash
# Naviguer vers le dossier du projet
cd MyRCSetup

# Build debug
./gradlew assembleDebug

# Build release (nécessite un keystore configuré)
./gradlew assembleRelease

# Nettoyer le projet
./gradlew clean

# Build complet avec tests
./gradlew build
```

## Tests et Débogage

### Exécution sur Émulateur
1. **Créer un AVD** :
   - Menu : `Tools` → `AVD Manager`
   - Créez un appareil virtuel avec API 24 minimum
   - Recommandé : Pixel 6 avec API 34

2. **Lancer l'application** :
   - Sélectionnez l'émulateur dans la liste
   - Cliquez sur le bouton "Run" (triangle vert)
   - Raccourci : `Shift+F10`

### Test sur Appareil Physique
1. **Activer le mode développeur** sur l'appareil :
   - Paramètres → À propos → Appuyez 7 fois sur "Numéro de build"
   
2. **Activer le débogage USB** :
   - Paramètres → Options de développement → Débogage USB

3. **Connecter l'appareil** via USB et autoriser le débogage

### Débogage
- **Logs** : Utilisez Logcat dans Android Studio
- **Points d'arrêt** : Cliquez dans la marge gauche de l'éditeur
- **Débogueur** : Menu `Run` → `Debug 'app'`

## Optimisation et Release

### Configuration Release
1. **Minification** : Activée dans `app/build.gradle.kts`
2. **Obfuscation** : Configurée via ProGuard
3. **Optimisation** : Build tools optimisent automatiquement

### Signature de l'APK
1. **Générer un keystore** :
   ```bash
   keytool -genkey -v -keystore my-release-key.keystore -alias my-key-alias -keyalg RSA -keysize 2048 -validity 10000
   ```

2. **Configurer dans Android Studio** :
   - Menu : `Build` → `Generate Signed Bundle / APK`
   - Suivez l'assistant de signature

### Vérification de l'APK
```bash
# Analyser la taille de l'APK
./gradlew analyzeReleaseBundle

# Vérifier la signature
jarsigner -verify -verbose -certs app-release.apk
```

## Résolution de Problèmes

### Erreurs Communes

#### Erreur de Synchronisation Gradle
```
Solution : File → Invalidate Caches and Restart
```

#### Erreur de Compilation Kotlin
```
Solution : Build → Clean Project puis Build → Rebuild Project
```

#### Problème de Dépendances
```
Solution : Vérifier les versions dans app/build.gradle.kts
```

#### Erreur de Mémoire
```
Solution : Augmenter la mémoire dans gradle.properties :
org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8
```

### Logs de Débogage
- **Gradle** : Onglet "Build" dans Android Studio
- **Application** : Onglet "Logcat"
- **Erreurs** : Onglet "Problems"

## Structure des Fichiers de Build

```
MyRCSetup/
├── build.gradle.kts          # Configuration projet racine
├── settings.gradle.kts       # Configuration modules
├── gradle.properties         # Propriétés Gradle
├── gradlew                   # Wrapper Gradle (Unix)
├── gradlew.bat              # Wrapper Gradle (Windows)
└── app/
    ├── build.gradle.kts     # Configuration module app
    ├── proguard-rules.pro   # Règles ProGuard
    └── src/                 # Code source
```

## Performance et Optimisation

### Temps de Build
- **Première compilation** : 2-5 minutes
- **Compilations incrémentales** : 30-60 secondes
- **Clean build** : 1-3 minutes

### Taille de l'APK
- **Debug** : ~8-12 MB
- **Release** : ~6-8 MB (avec minification)

---

**Note** : Ces instructions sont valides pour Android Studio Hedgehog et versions ultérieures. Pour des versions antérieures, certaines étapes peuvent différer légèrement.