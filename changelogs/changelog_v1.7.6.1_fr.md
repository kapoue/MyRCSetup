# Changelog v1.7.6.1 - Correction Conflit Manifeste

**Date de release :** 25 septembre 2025  
**Build :** 30  
**Type :** Correction technique

## 🔧 Correction Conflit de Manifeste

### ❌ Problème Identifié
Erreur de compilation lors du build Android Studio :
```
Attribute activity#com.journeyapps.barcodescanner.CaptureActivity@screenOrientation value=(portrait) 
is also present at [com.journeyapps:zxing-android-embedded:4.3.0] AndroidManifest.xml:50:13-56 value=(sensorLandscape).
```

### ✅ Solution Implémentée
Ajout de l'attribut `tools:replace="android:screenOrientation"` pour résoudre le conflit entre :
- **Notre configuration** : `android:screenOrientation="portrait"`
- **Bibliothèque ZXing** : `android:screenOrientation="sensorLandscape"`

### 🔧 Modification Technique

#### **AndroidManifest.xml**
```xml
<activity
    android:name="com.journeyapps.barcodescanner.CaptureActivity"
    android:screenOrientation="portrait"
    android:stateNotNeeded="true"
    android:theme="@style/zxing_CaptureTheme"
    android:windowSoftInputMode="stateAlwaysHidden"
    tools:replace="android:screenOrientation" />
```

### 📱 Résultat
- ✅ **Compilation réussie** : Plus d'erreur de conflit de manifeste
- ✅ **Orientation Portrait** : Le scanner QR s'ouvre en mode Portrait comme prévu
- ✅ **Fonctionnalité préservée** : Toutes les fonctionnalités QR Code restent opérationnelles

## 🔄 Versions Mises à Jour
- `build.gradle.kts` : versionCode 30, versionName "1.7.6.1"
- `RCSessionViewModel.kt` : appVersion "1.7.6.1" (2 occurrences)

## 📋 Statut BATCH 4
- **BATCH 4 v1.7.6.1** : ✅ **TERMINÉ** - Scanner QR Code en mode Portrait avec compilation réussie

## 🎯 Prochaines Étapes
- **BATCH 5 v1.7.7** : Corriger le crash du dialogue de sauvegarde

---

**Note :** Cette version corrective résout le conflit de manifeste tout en préservant l'orientation Portrait du scanner QR Code. La compilation fonctionne maintenant parfaitement.