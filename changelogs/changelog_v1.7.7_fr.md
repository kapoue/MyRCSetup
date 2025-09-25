# Changelog v1.7.7 - Correction du dialogue de sauvegarde

## 🐛 Corrections de bugs

### Dialogue de modifications non sauvées
- **Correction du crash du dialogue** : Restructuration complète du dialogue de confirmation pour éviter les crashes lors des actions de sauvegarde
- **Amélioration de la structure UI** : Remplacement de la structure Row problématique par une organisation plus robuste avec Column
- **Ajout de logs de débogage** : Intégration de logs Android pour faciliter le diagnostic des problèmes futurs
- **Gestion d'erreurs renforcée** : Suppression des blocs try-catch défaillants qui masquaient les vrais problèmes

### Système de gestion des versions
- **Centralisation des versions** : Création du fichier `AppConfig.kt` comme source unique de vérité pour toutes les informations de version
- **Cohérence garantie** : Toutes les versions (ViewModel, AboutScreen, exports) utilisent maintenant la même source centralisée
- **Maintenance simplifiée** : Plus besoin de mettre à jour les versions dans plusieurs fichiers

## 🔧 Améliorations techniques

### Architecture
- **AppConfig.kt** : Nouveau fichier de configuration centralisée contenant :
  - Version de l'application (1.7.7)
  - Code de version (31)
  - Version du format d'export (1.0)
  - Informations de copyright
- **Dialogue robuste** : Structure AlertDialog optimisée pour éviter les conflits de layout
- **Logs de débogage** : Ajout de traces pour faciliter le diagnostic des problèmes UI

## 📱 Fonctionnalités confirmées

### QR Code (testé et validé)
- ✅ **Transfert entre appareils** : Fonctionnalité testée avec succès entre deux smartphones
- ✅ **Génération QR** : Création de QR codes pour sessions individuelles
- ✅ **Scanner intégré** : Lecture QR codes en mode Portrait
- ✅ **Import automatique** : Intégration directe des sessions scannées

## 🎯 Résolution des problèmes

Cette version corrige définitivement :
- ❌ Crash "écran blanc" lors des actions du dialogue de sauvegarde
- ❌ Incohérences de versions entre différents écrans
- ❌ Structure de dialogue instable

## 📊 Statistiques de version

- **Version** : 1.7.7
- **Code de version** : 31
- **Corrections de bugs** : 3 problèmes majeurs résolus
- **Améliorations** : Système de configuration centralisée
- **Compatibilité** : Android 7.0+ (API 24)

---

**Note** : Cette version se concentre sur la stabilité et la robustesse du dialogue de sauvegarde, garantissant une expérience utilisateur fluide sans crashes.