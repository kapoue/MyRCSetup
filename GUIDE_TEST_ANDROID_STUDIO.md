# Guide de Test avec Android Studio

## 🚀 Test direct sans créer d'APK

### **Option 1 : Émulateur Android (Recommandé)**

#### Configuration de l'émulateur :
1. **Ouvrir Android Studio** avec le projet MyRCSetup
2. **Créer un émulateur** :
   - `Tools` → `AVD Manager` → `Create Virtual Device`
   - Choisir un téléphone (ex: Pixel 7) 
   - Sélectionner API Level 34 (Android 14)
   - Cliquer `Next` → `Finish`
3. **Lancer l'émulateur** : Cliquer sur ▶️ dans AVD Manager
4. **Lancer l'app** : Cliquer sur ▶️ `Run` dans Android Studio
5. **L'app s'installe automatiquement** sur l'émulateur

#### Avantages :
- ✅ Pas besoin de smartphone physique
- ✅ Test immédiat après modifications
- ✅ Débogage facile avec logs en temps réel
- ✅ Simulation de différents appareils

### **Option 2 : Smartphone en mode développeur**

#### Configuration du smartphone :
1. **Activer le mode développeur** :
   - `Paramètres` → `À propos du téléphone` → Appuyer 7x sur `Numéro de build`
2. **Activer le débogage USB** :
   - `Paramètres` → `Options développeur` → `Débogage USB` ✅
3. **Connecter le téléphone en USB** à votre PC
4. **Autoriser le débogage** quand la popup apparaît
5. **Lancer l'app** : Cliquer ▶️ `Run` → votre téléphone apparaîtra dans la liste

### **Option 3 : Installation rapide avec Gradle**

```bash
cd MyRCSetup
gradlew.bat installDebug
```

Cela installe directement l'APK sur l'appareil connecté (émulateur ou smartphone).

## 📱 Test des fonctionnalités Export/Import

### Étapes de test :
1. **Créer une session** avec le bouton `+`
2. **Remplir les champs** (nom voiture, circuit, etc.)
3. **Sauvegarder** la session
4. **Tester l'export** :
   - Menu `⋮` → `Exporter les données`
   - Devrait ouvrir le sélecteur d'applications
5. **Tester l'import** :
   - Menu `⋮` → `Importer les données`
   - Devrait ouvrir le sélecteur de fichiers

### Messages d'erreur :
- Les erreurs s'affichent en **Snackbar** en bas de l'écran
- Si aucune session : "Aucune session à exporter. Créez d'abord une session."
- Autres erreurs détaillées avec type d'exception

## 🔧 Débogage avancé

### Logs en temps réel :
1. **Ouvrir Logcat** : `View` → `Tool Windows` → `Logcat`
2. **Filtrer par app** : Sélectionner `com.myrcsetup.app`
3. **Voir les erreurs** en temps réel pendant les tests

### Hot Reload :
- Modifier le code → `Ctrl+F9` (Build) → Les changements s'appliquent immédiatement

## 🎯 Recommandation

**L'émulateur est la solution la plus pratique** pour le développement Android :
- Pas de configuration smartphone
- Test instantané
- Débogage complet
- Simulation de différents scénarios

## 🚨 Résolution de problèmes

### Si l'émulateur ne démarre pas :
1. Vérifier que la virtualisation est activée dans le BIOS
2. Installer Intel HAXM ou utiliser un émulateur ARM64

### Si le smartphone n'est pas détecté :
1. Installer les drivers USB du fabricant
2. Vérifier que le débogage USB est activé
3. Essayer un autre câble USB

### Si l'app ne s'installe pas :
```bash
# Nettoyer et rebuilder
gradlew.bat clean
gradlew.bat assembleDebug