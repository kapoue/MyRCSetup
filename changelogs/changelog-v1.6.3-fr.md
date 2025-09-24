# Changelog v1.6.3 - Correction Configuration Gradle

## 🐛 Corrections de Bugs

### Correction Configuration Build System
- **Ajout du plugin de sérialisation dans build.gradle.kts racine** : Le plugin `org.jetbrains.kotlin.plugin.serialization` version 1.9.20 est maintenant correctement déclaré au niveau projet
- **Résolution de l'erreur de compilation Android Studio** : Correction de "Plugin was not found in any of the following sources" lors de la synchronisation Gradle

## 📱 Informations Techniques

### Modifications du Build System
- Ajout du plugin `org.jetbrains.kotlin.plugin.serialization` version 1.9.20 dans le fichier `build.gradle.kts` racine
- Mise à jour de la version de l'application vers 1.6.3 (versionCode 15)
- Mise à jour de l'écran "À propos" avec les nouvelles informations de version

### Architecture Gradle
- **Niveau projet** : Déclaration du plugin de sérialisation avec version
- **Niveau application** : Application du plugin sans version (héritée du niveau projet)
- **Configuration complète** : Synchronisation Gradle maintenant fonctionnelle

## 🔧 Impact Utilisateur

Cette version corrige un problème de configuration Gradle qui empêchait la compilation dans Android Studio. Les développeurs peuvent maintenant :

1. **Ouvrir le projet** dans Android Studio sans erreur de synchronisation
2. **Compiler l'application** directement depuis l'IDE
3. **Utiliser toutes les fonctionnalités** d'export/import JSON
4. **Déboguer et tester** l'application en mode développement

## 📋 Notes de Déploiement

- **Mise à jour recommandée** : Essentielle pour la compilation avec Android Studio
- **Compatibilité** : Maintient la compatibilité avec toutes les versions précédentes
- **Développement** : Facilite le développement et les contributions futures

---

**Version** : 1.6.3  
**Code de version** : 15  
**Date de release** : 24 septembre 2025  
**Plateforme** : Android 7.0+ (API 24+)