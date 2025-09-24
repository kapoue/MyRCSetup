# Changelog v1.6.2 - Correction de la Sérialisation

## 🐛 Corrections de Bugs

### Correction Critique de l'Export/Import
- **Ajout du plugin de sérialisation Kotlin manquant** : Résolution de l'erreur "Serializer for class 'ExportData' is not found" qui empêchait l'export des données
- **Configuration complète du système de sérialisation** : Le plugin `org.jetbrains.kotlin.plugin.serialization` est maintenant correctement configuré dans le build system

## 📱 Informations Techniques

### Modifications du Build System
- Ajout du plugin `org.jetbrains.kotlin.plugin.serialization` dans `build.gradle.kts`
- Mise à jour de la version de l'application vers 1.6.2 (versionCode 14)
- Mise à jour de l'écran "À propos" avec les nouvelles informations de version

### Fonctionnalités Affectées
- ✅ **Export JSON** : Maintenant fonctionnel avec partage Android natif
- ✅ **Import JSON** : Sélecteur de fichier Android natif opérationnel
- ✅ **Sérialisation des données** : Conversion correcte des sessions RC en format JSON

## 🔧 Impact Utilisateur

Cette version corrige un problème critique qui empêchait complètement l'utilisation des fonctionnalités d'export et d'import introduites en v1.6.1. Les utilisateurs peuvent maintenant :

1. **Exporter leurs sessions** via le bouton "Exporter" dans le menu
2. **Partager leurs données** avec d'autres applications (email, cloud, etc.)
3. **Importer des sessions** depuis des fichiers JSON existants
4. **Sauvegarder leurs réglages** pour les restaurer plus tard

## 📋 Notes de Déploiement

- **Mise à jour recommandée** : Cette correction est essentielle pour tous les utilisateurs de v1.6.1
- **Compatibilité** : Maintient la compatibilité avec toutes les versions précédentes
- **Données** : Aucune migration de base de données requise

---

**Version** : 1.6.2  
**Code de version** : 14  
**Date de release** : 24 septembre 2025  
**Plateforme** : Android 7.0+ (API 24+)