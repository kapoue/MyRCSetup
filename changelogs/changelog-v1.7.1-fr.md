# Changelog v1.7.1 - Corrections de bugs critiques

**Date de sortie :** 24 septembre 2025

## 🐛 Corrections de bugs

### Interface utilisateur
- **Formatage automatique du temps au tour** : Le champ "Meilleur temps au tour" applique maintenant automatiquement le format mm:ss.ms lors de la saisie
- **Correction du double-clic** : Résolution du bug qui empêchait de visualiser une session en cliquant sur sa carte
- **Dialogue de modifications non sauvées** : Correction des crashes lors des actions "Sauvegarder", "Quitter sans sauvegarder" et "Annuler"
- **Gestion du bouton retour Android** : Le bouton retour du système affiche maintenant correctement le dialogue de confirmation pour les modifications non sauvées

### Fonctionnalités QR Code
- **Partage automatique** : Les QR codes sont maintenant automatiquement partagés dès leur génération, sans action supplémentaire
- **Import simplifié** : L'import via QR Code va maintenant directement à la caméra sans écran intermédiaire
- **Mode Portrait** : Le scanner QR Code est maintenant verrouillé en mode portrait pour une meilleure expérience

### Versions et métadonnées
- **Cohérence des versions** : Correction de toutes les incohérences de numéros de version dans l'application
- **Export JSON** : La version correcte (1.7.1) est maintenant affichée dans les exports JSON
- **Écran À propos** : Mise à jour vers la version 1.7.1 et code de version 18

## 🔧 Améliorations techniques

### Validation des données
- **Filtrage des caractères** : Le champ temps au tour n'accepte plus que les chiffres, deux-points et points
- **Formatage intelligent** : Application automatique du format mm:ss.ms pendant la saisie

### Gestion des erreurs
- **Protection contre les crashes** : Ajout de try-catch dans les actions du dialogue pour éviter les plantages
- **Gestion des exceptions** : Amélioration de la robustesse de l'interface utilisateur

### Navigation
- **BackHandler** : Intégration native du gestionnaire de bouton retour Android
- **Flux utilisateur** : Amélioration de la cohérence dans la navigation entre les écrans

## 📱 Compatibilité

- **Android minimum** : 7.0 (API 24)
- **Android cible** : 14 (API 35)
- **Architecture** : ARM64, ARM32, x86_64

## 🔄 Migration

Cette version est entièrement compatible avec les données des versions précédentes. Aucune action particulière n'est requise lors de la mise à jour.

## 📋 Notes techniques

- **Version code** : 18
- **Taille APK** : ~8 MB
- **Permissions** : Caméra (pour QR codes), Stockage (pour import/export)

---

**Installation :** Téléchargez l'APK depuis les [releases GitHub](https://github.com/kapoue/MyRCSetup/releases) ou compilez depuis le code source.