# Changelog v1.7.6 - Scanner QR Code en Mode Portrait

**Date de release :** 25 septembre 2025  
**Build :** 29  
**Type :** Amélioration UX

## 🔄 BATCH 4 - Orientation Scanner QR

### ✅ Problème Résolu
- **Scanner QR en mode Landscape** : Le scanner QR s'ouvrait en mode paysage, rendant difficile le scan de QR codes affichés sur d'autres téléphones
- **Ergonomie améliorée** : Le scanner s'ouvre maintenant en mode Portrait pour une meilleure expérience utilisateur

### 🔧 Modifications Techniques

#### **AndroidManifest.xml**
Ajout de la configuration explicite pour l'activité de scan ZXing :
```xml
<activity
    android:name="com.journeyapps.barcodescanner.CaptureActivity"
    android:screenOrientation="portrait"
    android:stateNotNeeded="true"
    android:theme="@style/zxing_CaptureTheme"
    android:windowSoftInputMode="stateAlwaysHidden" />
```

#### **Configuration Complète**
- `android:screenOrientation="portrait"` : Force l'orientation Portrait
- `android:stateNotNeeded="true"` : Optimise les performances
- `android:theme="@style/zxing_CaptureTheme"` : Utilise le thème ZXing standard
- `android:windowSoftInputMode="stateAlwaysHidden"` : Masque le clavier virtuel

### 📱 Amélioration UX
- **Avant** : Scanner en mode Landscape (difficile à utiliser)
- **Après** : Scanner en mode Portrait (ergonomique et naturel)
- **Impact** : Facilite grandement le scan de QR codes entre appareils

## 🔄 Versions Mises à Jour
- `build.gradle.kts` : versionCode 29, versionName "1.7.6"
- `RCSessionViewModel.kt` : appVersion "1.7.6" (2 occurrences)

## 📋 Statut BATCH 4
- **BATCH 4 v1.7.6** : ✅ **TERMINÉ** - Scanner QR Code en mode Portrait

## 🎯 Prochaines Étapes
- **BATCH 5 v1.7.7** : Corriger le crash du dialogue de sauvegarde

---

**Note :** Cette version améliore significativement l'ergonomie du scanner QR Code en forçant l'orientation Portrait, rendant le scan entre appareils beaucoup plus pratique.