# Changelog v1.7.3 - BATCH 2 : Correction du Bug de Double-Clic

## 🔧 Corrections

### Interface Utilisateur
- **Bug de double-clic corrigé** : Suppression du conflit entre gestionnaires de clic
  - Élimination du `.clickable` redondant sur la Column dans SessionCard
  - Conservation uniquement de l'IconButton pour l'édition
  - Navigation vers l'édition maintenant fiable en un seul clic
  - Amélioration de la réactivité de l'interface

### Technique
- Mise à jour des numéros de version dans tous les fichiers
- Version d'export JSON corrigée vers 1.7.3

## 📋 Notes de Version
Cette version fait partie du BATCH 2 de corrections suite aux retours utilisateur sur v1.7.1. Le problème de double-clic était causé par deux gestionnaires de clic qui se chevauchaient dans les cartes de session.

## 🔄 Prochaines Corrections
- BATCH 3 v1.7.4 : Correction du bouton QR Code non fonctionnel
- BATCH 4 v1.7.5 : Correction de l'orientation du scanner QR
- BATCH 5 v1.7.6 : Correction du crash du dialogue de sauvegarde

---
**Version** : 1.7.3  
**Code de version** : 20  
**Date** : 24 septembre 2025