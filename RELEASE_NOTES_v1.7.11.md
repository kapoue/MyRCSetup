# My RC Setup - Release Notes v1.7.11
## Mise à jour majeure : De la v1.4.1 à la v1.7.11

---

## 📱 Installation de l'APK

### Prérequis
- Android 7.0 (API 24) ou supérieur
- Autoriser l'installation d'applications de sources inconnues

### Instructions d'installation

1. **Télécharger l'APK**
   - Rendez-vous sur la page [Releases GitHub](https://github.com/kapouik/MyRCSetup/releases)
   - Téléchargez le fichier `MyRCSetup-v1.7.11.apk`

2. **Autoriser les sources inconnues**
   - Allez dans `Paramètres` > `Sécurité` > `Sources inconnues`
   - Activez l'option pour permettre l'installation d'applications tierces
   - Ou autorisez spécifiquement votre navigateur/gestionnaire de fichiers

3. **Installer l'application**
   - Ouvrez le fichier APK téléchargé
   - Suivez les instructions d'installation
   - Acceptez les permissions demandées

4. **Première utilisation**
   - Lancez l'application "My RC Setup"
   - L'application est prête à utiliser immédiatement
   - Aucune configuration initiale requise

### Permissions requises
- **Caméra** : Pour scanner les QR codes de partage de sessions
- **Stockage** : Pour l'export/import de données JSON

---

## 🚀 Nouvelles fonctionnalités majeures depuis v1.4.1

### 🎨 Interface utilisateur modernisée (v1.5.x)
- **Thème "Racing Joyeuse"** avec palette de couleurs orange, bleu électrique et vert lime
- **Icônes par section** pour une navigation intuitive
- **Amélioration visuelle** des cartes de session avec bordures et ombres
- **Correction de la barre de statut** pour une meilleure lisibilité

### 📊 Système de sauvegarde et partage (v1.6.x)
- **Export/Import JSON** avec noms de fichiers horodatés
- **Partage Android natif** via Intent.ACTION_SEND
- **Sélecteur de fichiers** pour l'import de données
- **Écran "À propos"** avec informations complètes de l'application

### 📱 Partage par QR Code (v1.7.0)
- **Génération de QR codes** pour partager des sessions individuelles
- **Scanner QR intégré** pour importer des sessions depuis d'autres appareils
- **Partage instantané** entre smartphones sans connexion Internet
- **Mode Portrait** pour le scanner QR

### 🔧 Améliorations de l'expérience utilisateur (v1.7.x)
- **Navigation au clavier** optimisée avec défilement automatique
- **Dialogue de confirmation** pour les modifications non sauvées
- **Correction des bugs** de double-clic et de navigation
- **Système de highlight** pour les sessions nouvellement créées/modifiées
- **Scroll automatique** vers les nouvelles sessions avec mise en évidence

---

## 📋 Changelog détaillé par version

### Version 1.7.11 - Amélioration du scroll et highlight
🔧 **AMÉLIORATIONS :**
• Correction du scroll vers le haut pour les nouvelles sessions
• Augmentation du délai de highlight de 1 à 2 secondes
• Ajout d'un délai de 100ms avant le scroll pour attendre la mise à jour de la liste
• Amélioration des logs de débogage pour le scroll
• Meilleure synchronisation entre la sauvegarde et l'affichage

📝 **DÉTAILS TECHNIQUES :**
• Ajout de la dépendance sessions.size dans LaunchedEffect pour détecter les changements
• Délai de 100ms avant le scroll pour laisser le temps à la liste de se mettre à jour
• Logs détaillés pour diagnostiquer les problèmes de scroll
• Highlight maintenant visible pendant 2 secondes au lieu de 1

### Version 1.7.10 - Correction du système de highlight
🔧 **CORRECTIONS :**
• Correction du système de highlight des sessions sauvées
• Séparation de la logique de scroll et de highlight
• Ajout de logs de débogage pour le suivi des états
• Amélioration de la visibilité des sessions nouvellement créées/modifiées

📝 **DÉTAILS TECHNIQUES :**
• Correction de la fonction clearScrollAndHighlight() qui effaçait le highlight trop rapidement
• Séparation en clearScrollToSessionId() et maintien du highlight pendant 1 seconde
• Ajout de logs pour diagnostiquer les problèmes de highlight
• Amélioration de la gestion des états dans RCSessionViewModel

### Version 1.7.9 - Système de scroll et highlight automatiques
🆕 **NOUVELLES FONCTIONNALITÉS :**
• Scroll automatique vers les nouvelles sessions créées
• Highlight vert lime des sessions sauvées pendant 1 seconde
• Scroll intelligent vers la position des sessions modifiées
• Animation fluide avec fade pour le highlight

📝 **DÉTAILS TECHNIQUES :**
• Ajout de highlightedSessionId et scrollToSessionId dans RCSessionUiState
• Implémentation de LazyListState pour contrôler le scroll
• Animation avec animateColorAsState pour le highlight
• Gestion des états avec LaunchedEffect et coroutines

### Version 1.7.8 - Correction critique de navigation
🔧 **CORRECTION CRITIQUE :**
• Correction du bug d'écran blanc après sauvegarde de session
• Amélioration de la logique de navigation post-sauvegarde
• Suppression des conflits de LaunchedEffect multiples

📝 **DÉTAILS TECHNIQUES :**
• Restructuration de la logique de navigation dans SessionFormScreen
• Suppression de l'assignation conflictuelle currentSession = null
• Amélioration de la gestion des états de navigation

### Version 1.7.7 - Corrections multiples
🔧 **CORRECTIONS :**
• Correction du crash du dialogue de sauvegarde
• Amélioration de la gestion des actions de dialogue
• Correction des conflits de noms AppConfig/BuildConfig
• Centralisation de la gestion des versions avec AppConfig.kt

### Version 1.7.6 - Scanner QR en mode Portrait
🔧 **CORRECTIONS :**
• Correction de l'orientation du scanner QR (forcé en Portrait)
• Amélioration de l'expérience utilisateur pour le scan
• Stabilisation de l'interface de scan

### Version 1.7.5.2 - Bouton QR Code fonctionnel
🔧 **CORRECTIONS :**
• Correction du bouton QR Code non fonctionnel
• Affichage direct du QR code au lieu de l'erreur
• Amélioration de la génération des QR codes

### Version 1.7.3 - Correction du double-clic
🔧 **CORRECTIONS :**
• Correction du bug de double-clic pour visualiser une session
• Suppression des gestionnaires d'événements conflictuels
• Amélioration de la réactivité de l'interface

### Version 1.7.2 - Champ de temps simplifié
🔧 **CORRECTIONS :**
• Retour au champ de saisie de temps en texte simple
• Suppression du formatage automatique problématique
• Amélioration de la facilité de saisie

### Version 1.7.1 - Corrections multiples QR Code
🔧 **CORRECTIONS :**
• Simplification de l'import QR Code (accès direct à la caméra)
• Correction du bouton QR Code non fonctionnel
• Ajout de la gestion du retour Android pour le dialogue
• Correction des crashes lors des actions de sauvegarde

### Version 1.7.0 - Partage par QR Code
🆕 **NOUVELLES FONCTIONNALITÉS :**
• Génération de QR codes pour partager des sessions individuelles
• Scanner QR intégré pour importer des sessions
• Bouton QR Code dans la barre d'outils de modification
• Menu "Importer via QR Code" dans l'écran principal
• Dialogue de confirmation pour les modifications non sauvées

📝 **DÉTAILS TECHNIQUES :**
• Intégration de la bibliothèque ZXing pour QR codes
• Permissions caméra ajoutées dans AndroidManifest
• Utilitaires QRCodeGenerator et QRCodeScanner
• Gestion des états de modification avec dialogue de confirmation

### Version 1.6.3 - Correction de sérialisation
🔧 **CORRECTIONS :**
• Ajout du plugin de sérialisation Kotlin dans build.gradle.kts racine
• Correction des erreurs de compilation liées à la sérialisation
• Amélioration de la stabilité de l'export/import JSON

### Version 1.6.2 - Corrections mineures
🔧 **CORRECTIONS :**
• Ajout du plugin de sérialisation Kotlin manquant
• Correction de l'affichage de version dans l'écran À propos
• Amélioration de la gestion des dépendances

### Version 1.6.1 - Export/Import Android natif
🔧 **AMÉLIORATIONS :**
• Implémentation de l'export Android avec Intent.ACTION_SEND (partage)
• Implémentation de l'import Android avec sélecteur de fichier
• Correction du copyright 2025 dans l'écran À propos
• Amélioration de l'intégration système Android

### Version 1.6.0 - Système de sauvegarde
🆕 **NOUVELLES FONCTIONNALITÉS :**
• Export JSON avec nom de fichier horodaté
• Import JSON avec validation des données
• Menu "3 points" dans la barre d'outils principale
• Écran "À propos" avec informations complètes
• Masquage du champ date/heure (automatique)

🔧 **CORRECTIONS :**
• Correction du bug de corruption des dates
• Amélioration de la gestion des timestamps

### Version 1.5.3 - Correction de la barre de statut
🔧 **CORRECTIONS :**
• Suppression du SystemUiController conflictuel dans MainActivity
• Correction définitive de la visibilité des icônes de la barre de statut
• Amélioration de la stabilité de l'interface

### Version 1.5.2 - Interface Racing Joyeuse
🎨 **AMÉLIORATIONS VISUELLES :**
• Finalisation de la correction de la barre de statut avec icônes foncées
• Amélioration de la séparation visuelle des sessions
• Changement de la couleur de sélection du rouge vers le bleu
• Ajout de bordures et ombres aux cartes de session

### Version 1.5.1 - Corrections visuelles
🔧 **CORRECTIONS :**
• Correction du bouton FloatingActionButton "+" cassé
• Amélioration de la visibilité des éléments d'interface
• Stabilisation des composants visuels

### Version 1.4.3 - Navigation au clavier
🆕 **NOUVELLES FONCTIONNALITÉS :**
• Navigation au clavier optimisée avec ImeAction approprié
• Défilement automatique pour les champs masqués
• Connexion du dernier champ à la sauvegarde automatique
• Suppression du champ ratio final (calculé automatiquement)

🔧 **AMÉLIORATIONS :**
• Modification des champs pincement et anti-roulis en numériques
• Amélioration de l'ergonomie de saisie

### Version 1.4.2 - Correction de la barre de statut
🔧 **CORRECTIONS :**
• Correction de la visibilité du texte de la barre de statut
• Ajout de la dépendance SystemUiController
• Forçage du texte blanc sur fond transparent

---

## 🎯 Fonctionnalités principales de l'application

### Gestion des sessions d'entraînement
- **Informations de base** : Nom de la voiture, circuit, date/heure automatique, meilleur temps
- **Suspension** : Ressorts avant/arrière, huile d'amortisseur, position des amortisseurs
- **Différentiels** : Huile avant/arrière/central
- **Géométrie** : Carrossage, pincement, chasse
- **Transmission** : Pignon moteur, couronne
- **Pneus** : Marque/modèle avant/arrière, mousse, additif d'adhérence
- **Châssis** : Rigidité, hauteur de caisse, anti-roulis

### Fonctionnalités avancées
- **Stockage local** avec base de données SQLite
- **Export/Import JSON** pour sauvegarde et partage
- **Partage QR Code** entre appareils
- **Interface moderne** avec thème Racing Joyeuse
- **Navigation optimisée** au clavier
- **Scroll et highlight automatiques** pour les nouvelles sessions

---

## 🔧 Support technique

### Configuration minimale
- **Android** : 7.0 (API 24) ou supérieur
- **RAM** : 2 GB recommandé
- **Stockage** : 50 MB d'espace libre
- **Caméra** : Requise pour les fonctionnalités QR Code

### Résolution de problèmes
1. **L'application ne s'installe pas** : Vérifiez que les sources inconnues sont autorisées
2. **Crash au démarrage** : Redémarrez l'appareil et relancez l'application
3. **QR Code ne fonctionne pas** : Vérifiez les permissions caméra dans les paramètres
4. **Export/Import échoue** : Vérifiez les permissions de stockage

### Contact et support
- **GitHub** : [https://github.com/kapouik/MyRCSetup](https://github.com/kapouik/MyRCSetup)
- **Issues** : Signalez les bugs via GitHub Issues
- **Licence** : MIT License

---

## 📈 Statistiques de développement

- **Versions publiées** : 35 versions de v1.4.1 à v1.7.11
- **Corrections de bugs** : Plus de 50 corrections majeures
- **Nouvelles fonctionnalités** : 15+ fonctionnalités ajoutées
- **Améliorations UX** : Interface complètement repensée
- **Compatibilité** : Android 7.0+ (95% des appareils)

---

*My RC Setup v1.7.11 - Application de gestion des réglages de voitures RC*  
*Développée avec ❤️ pour la communauté RC*  
*© 2025 - Licence MIT*