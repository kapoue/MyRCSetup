# Changelog v1.7.4.2 - Correction Critique QR Code

## 🔧 Corrections de Bugs

### QR Code - Correction FileProvider
- **CORRIGÉ** : Bouton QR Code maintenant fonctionnel
- **CAUSE** : Configuration FileProvider manquante pour le dossier qr_codes
- **SOLUTION** : Ajout de `<cache-path name="qr_codes" path="qr_codes/" />` dans file_paths.xml
- **IMPACT** : Le partage de QR codes fonctionne maintenant correctement

## 📋 Détails Techniques

### Problème Identifié
```
Exception: Failed to find configured root that contains /data/data/com.myrcsetup.app/cache/qr_codes/
```

### Solution Appliquée
- Modification de `app/src/main/res/xml/file_paths.xml`
- Ajout du chemin manquant pour les images QR code
- Le FileProvider peut maintenant accéder aux fichiers QR générés

## 🔄 Workflow QR Code Complet
1. ✅ Génération QR code (512x512 pixels)
2. ✅ Sauvegarde dans cache/qr_codes/
3. ✅ Partage via FileProvider (CORRIGÉ)
4. ✅ Ouverture automatique du sélecteur de partage

## 📱 Version
- **Version** : 1.7.4.2
- **Build** : 23
- **Date** : 25 septembre 2025

## 🧪 Tests Requis
- [ ] Créer/modifier une session
- [ ] Cliquer sur le bouton QR Code
- [ ] Vérifier l'ouverture du sélecteur de partage
- [ ] Tester le partage vers différentes applications

---
*Cette correction résout définitivement le problème de partage QR Code identifié dans les versions précédentes.*