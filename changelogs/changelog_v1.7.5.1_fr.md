# Changelog v1.7.5.1 - Correction Debug QR Code

**Date de release :** 25 septembre 2025  
**Version :** 1.7.5.1 (Build 27)  
**Type :** Correction Debug

## 🔍 Problème Identifié

Après les tests utilisateur de la v1.7.5, le dialogue QR Code ne s'affiche toujours pas malgré la génération réussie du QR code.

### 📊 Analyse des Logs
Les logs montrent que :
- ✅ Le bouton QR Code est cliqué correctement
- ✅ La fonction `generateQRCodeForSession()` est appelée
- ✅ Le QR code est généré avec succès (512x512)
- ❌ Le dialogue ne s'affiche pas à l'écran

## 🛠️ Corrections Apportées

### **Debug Supplémentaire Ajouté**
- Ajout de logs détaillés dans `RCSessionViewModel.kt` :
  - `"Setting showQRCodeDialog = true"`
  - `"QR dialog state updated: ${_uiState.value.showQRCodeDialog}"`
- Ces logs permettront d'identifier si le problème vient de :
  - La mise à jour de l'état dans le ViewModel
  - L'observation de l'état dans l'UI
  - La condition d'affichage du dialogue

### **Versions Mises à Jour**
- `build.gradle.kts` : versionCode 27, versionName "1.7.5.1"

## 🎯 Objectif de cette Version

Cette version de debug permettra d'identifier précisément où se situe le problème dans le workflow QR Code :

1. **Si les nouveaux logs apparaissent** : Le problème est dans l'UI (observation de l'état)
2. **Si les nouveaux logs n'apparaissent pas** : Le problème est dans la mise à jour de l'état

## 📋 Instructions de Test

1. Sauvegarder une session
2. Cliquer sur le bouton QR Code
3. Vérifier les logs pour :
   - `"Setting showQRCodeDialog = true"`
   - `"QR dialog state updated: true"`
4. Rapporter si le dialogue s'affiche ou non

## 🔄 Prochaines Étapes

Selon les résultats des logs, nous procéderons à :
- **Cas 1** : Correction de l'observation de l'état dans l'UI
- **Cas 2** : Correction de la mise à jour de l'état dans le ViewModel

---

**Note :** Cette version est spécifiquement conçue pour le diagnostic et sera suivie d'une correction définitive.