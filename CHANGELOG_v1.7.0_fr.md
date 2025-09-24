# Changelog Version 1.7.0 - "QR Code & UX"

## 🎯 Nouvelles Fonctionnalités

### 📱 Partage QR Code
- **Bouton QR Code** dans la barre d'outils des sessions sauvegardées
- **Génération automatique** de QR codes pour partager vos réglages
- **Partage direct** du QR code via les applications installées
- **Optimisation intelligente** de la taille du QR code selon les données

### 📥 Import QR Code
- **Menu "Importer via QR Code"** dans l'écran principal
- **Scanner intégré** avec gestion des permissions caméra
- **Validation automatique** des QR codes de sessions RC
- **Import direct** des réglages scannés dans votre collection

### 💾 Dialogue de Confirmation
- **Protection contre la perte** de modifications non sauvées
- **Dialogue intelligent** lors de la navigation retour
- **Options flexibles** : Sauvegarder et quitter, Quitter sans sauver, Annuler
- **Détection automatique** des changements dans le formulaire

## 🔧 Améliorations Techniques

### 📚 Nouvelles Dépendances
- **ZXing Android Embedded 4.3.0** pour la génération/scan QR
- **ZXing Core 3.5.3** pour le traitement des codes
- **CameraX** pour l'accès caméra moderne

### 🔐 Permissions
- **Permission CAMERA** ajoutée pour le scan QR
- **Gestion gracieuse** des refus de permission
- **Interface informative** pour guider l'utilisateur

### 🏗️ Architecture
- **QRCodeGenerator** : Utilitaire de génération avec couleurs personnalisées
- **QRCodeScanner** : Composables Jetpack Compose pour le scan
- **Validation robuste** des données QR scannées
- **Gestion d'état** améliorée dans le ViewModel

## 📊 Données & Export

### 🔄 Version Export
- **Version 1.7.0** dans les exports JSON
- **Compatibilité** maintenue avec les versions précédentes
- **Métadonnées** enrichies pour le suivi des versions

### 🎨 Interface Utilisateur
- **Icône QR Code** dans la TopAppBar des sessions
- **Interface de scan** moderne et intuitive
- **Messages d'erreur** explicites et utiles
- **Feedback visuel** pour toutes les actions

## 🐛 Corrections

### 🔧 Stabilité
- **Correction** des erreurs de compilation QR Code
- **Validation** des paramètres de composables
- **Gestion d'erreur** robuste pour les opérations QR

### 📱 Compatibilité
- **Support Android 7.0+** (API 24) maintenu
- **Optimisation** pour différentes tailles d'écran
- **Performance** améliorée pour la génération QR

## 🚀 Utilisation

### Partager une Session
1. Ouvrez une session sauvegardée
2. Cliquez sur l'icône QR Code dans la barre d'outils
3. Partagez le QR code généré via vos applications

### Importer une Session
1. Menu principal → "Importer via QR Code"
2. Autorisez l'accès caméra si demandé
3. Scannez le QR code d'une session
4. La session est automatiquement importée

### Protection des Données
- Vos modifications sont automatiquement détectées
- Un dialogue vous propose de sauvegarder avant de quitter
- Aucune perte de données accidentelle

---

**Version** : 1.7.0  
**Code Version** : 16  
**Date** : Janvier 2025  
**Compatibilité** : Android 7.0+ (API 24)  
**Taille** : ~8 MB