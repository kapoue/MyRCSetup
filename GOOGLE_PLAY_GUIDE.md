# Guide de Publication sur Google Play Store - My RC Setup

## Préparation de l'Application

### 1. Finalisation du Code

#### Vérifications Obligatoires
- [ ] Tests complets sur différents appareils
- [ ] Vérification des permissions dans `AndroidManifest.xml`
- [ ] Optimisation des performances
- [ ] Validation de l'interface sur différentes tailles d'écran
- [ ] Test de la base de données et des migrations

#### Configuration Release
```kotlin
// Dans app/build.gradle.kts
buildTypes {
    release {
        isMinifyEnabled = true
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
        signingConfig = signingConfigs.getByName("release")
    }
}
```

### 2. Signature de l'Application

#### Création du Keystore
```bash
keytool -genkey -v -keystore my-rc-setup-release.keystore \
    -alias my-rc-setup \
    -keyalg RSA \
    -keysize 2048 \
    -validity 25000
```

#### Configuration dans Android Studio
1. Menu : `Build` → `Generate Signed Bundle / APK`
2. Sélectionnez "Android App Bundle" (recommandé)
3. Créez ou sélectionnez votre keystore
4. Remplissez les informations :
   - **Key store password** : [votre mot de passe]
   - **Key alias** : my-rc-setup
   - **Key password** : [votre mot de passe]

#### Sauvegarde Sécurisée
⚠️ **IMPORTANT** : Sauvegardez votre keystore en lieu sûr !
- Sans le keystore, vous ne pourrez plus mettre à jour l'application
- Stockez-le dans un endroit sécurisé (cloud chiffré, coffre-fort numérique)

### 3. Génération de l'App Bundle

```bash
# Via ligne de commande
./gradlew bundleRelease

# Le fichier sera généré dans :
# app/build/outputs/bundle/release/app-release.aab
```

## Création du Compte Développeur

### 1. Inscription Google Play Console
1. Rendez-vous sur : https://play.google.com/console
2. Connectez-vous avec votre compte Google
3. Payez les frais d'inscription : 25€ (paiement unique)
4. Acceptez les conditions d'utilisation

### 2. Configuration du Profil
- **Nom du développeur** : Votre nom ou nom de société
- **Adresse** : Adresse complète obligatoire
- **Informations fiscales** : Selon votre situation
- **Compte bancaire** : Pour les revenus (si app payante)

## Préparation des Assets

### 1. Icônes et Screenshots

#### Icône de l'Application
- **Format** : PNG 32 bits
- **Taille** : 512 x 512 pixels
- **Pas de transparence** pour l'icône principale
- **Design** : Suivre les guidelines Material Design

#### Screenshots Obligatoires
- **Téléphone** : Au moins 2 screenshots
  - Taille : 320-3840 pixels (largeur ou hauteur)
  - Format : PNG ou JPEG
  - Recommandé : 1080 x 1920 pixels

#### Screenshots Optionnels
- **Tablette 7 pouces** : 1024 x 1600 pixels
- **Tablette 10 pouces** : 1280 x 1920 pixels

### 2. Graphiques Promotionnels

#### Feature Graphic (Obligatoire)
- **Taille** : 1024 x 500 pixels
- **Format** : PNG ou JPEG
- **Contenu** : Logo + texte descriptif

#### Icône TV (Si applicable)
- **Taille** : 1280 x 720 pixels
- **Format** : PNG

### 3. Descriptions et Métadonnées

#### Titre de l'Application
```
My RC Setup - Réglages Voitures RC
```
(Maximum 50 caractères)

#### Description Courte
```
Gérez et sauvegardez tous les réglages de vos voitures RC pour optimiser vos performances sur circuit.
```
(Maximum 80 caractères)

#### Description Complète
```
🏁 MY RC SETUP - L'APPLICATION INDISPENSABLE POUR LES PASSIONNÉS DE VOITURES RC

Optimisez vos performances sur circuit en gérant facilement tous les réglages techniques de vos voitures radiocommandées !

✨ FONCTIONNALITÉS PRINCIPALES
• Sauvegarde complète des réglages par session d'entraînement
• Gestion détaillée de la suspension (ressorts, huiles, positions)
• Configuration des différentiels (avant, arrière, central)
• Réglages de géométrie (carrossage, pincement, chasse)
• Paramètres de transmission avec calcul automatique du rapport
• Gestion des pneus et additifs d'adhérence
• Configuration châssis (rigidité, hauteurs, anti-roulis)

🎯 INTERFACE MODERNE
• Design Material Design 3
• Navigation intuitive et rapide
• Formulaires ergonomiques avec validation
• Thème adaptatif (clair/sombre)

💾 STOCKAGE SÉCURISÉ
• Base de données locale (aucune connexion requise)
• Sauvegarde automatique des modifications
• Historique complet de vos sessions
• Recherche et tri des sessions

🏆 POUR QUI ?
• Pilotes de voitures RC débutants et experts
• Clubs de modélisme
• Compétiteurs recherchant la performance optimale
• Passionnés souhaitant tracker leurs réglages

📱 COMPATIBILITÉ
• Android 7.0 minimum
• Fonctionne sur smartphones et tablettes
• Aucune permission spéciale requise
• Application 100% offline

Téléchargez My RC Setup et donnez une nouvelle dimension à vos sessions d'entraînement !

🔧 Réglages précis = Performances maximales
```

## Publication sur Google Play

### 1. Création de l'Application

#### Dans Google Play Console
1. Cliquez sur "Créer une application"
2. Remplissez les informations :
   - **Nom** : My RC Setup
   - **Langue par défaut** : Français
   - **Type** : Application
   - **Gratuite ou payante** : Gratuite

### 2. Configuration de l'Application

#### Contenu de l'Application
1. **Classification du contenu** :
   - Public cible : Tous les âges
   - Contenu : Aucun contenu sensible

2. **Politique de confidentialité** :
   - URL obligatoire (même pour apps offline)
   - Exemple de contenu simple pour app offline

3. **Autorisations** :
   - Déclarer toutes les permissions utilisées
   - My RC Setup : Aucune permission spéciale

#### Fiche Play Store
1. **Détails de l'application** :
   - Titre, description courte et complète
   - Catégorie : "Sports" ou "Outils"
   - Tags : voiture rc, modélisme, réglages, sport

2. **Assets graphiques** :
   - Téléchargez tous les screenshots
   - Ajoutez l'icône et le feature graphic

### 3. Version de l'Application

#### App Bundle
1. Allez dans "Version" → "Production"
2. Cliquez sur "Créer une version"
3. Téléchargez votre fichier `.aab`
4. Remplissez les notes de version :

```
Version 1.0.0 - Première version
• Gestion complète des réglages RC
• Interface Material Design 3
• Stockage local sécurisé
• Toutes les fonctionnalités de base
```

### 4. Test et Validation

#### Tests Internes (Recommandé)
1. Créez une liste de testeurs internes
2. Publiez une version de test
3. Testez pendant quelques jours
4. Corrigez les bugs éventuels

#### Tests Ouverts (Optionnel)
- Permet à un public plus large de tester
- Utile pour obtenir des retours avant la publication

### 5. Publication

#### Révision Google
1. Soumettez pour révision
2. Délai habituel : 1-3 jours ouvrés
3. Google vérifie :
   - Conformité aux politiques
   - Fonctionnement de l'application
   - Contenu approprié

#### Mise en Ligne
- Une fois approuvée, l'application sera disponible
- Délai de propagation : quelques heures
- Visible dans le monde entier (sauf restrictions)

## Après Publication

### 1. Suivi des Performances
- **Console Play** : Statistiques de téléchargement
- **Avis utilisateurs** : Répondez aux commentaires
- **Rapports de crash** : Surveillez les erreurs

### 2. Mises à Jour
```kotlin
// Incrémentez dans app/build.gradle.kts
versionCode = 2
versionName = "1.0.1"
```

### 3. Marketing
- **ASO** (App Store Optimization) : Optimisez titre et description
- **Réseaux sociaux** : Partagez dans les communautés RC
- **Forums spécialisés** : Présentez l'app aux passionnés

## Checklist Finale

### Avant Soumission
- [ ] Tests complets sur différents appareils
- [ ] App Bundle signé généré
- [ ] Screenshots de qualité préparés
- [ ] Descriptions rédigées et relues
- [ ] Politique de confidentialité en ligne
- [ ] Compte développeur configuré
- [ ] Keystore sauvegardé en sécurité

### Après Publication
- [ ] Vérifier la disponibilité sur le Play Store
- [ ] Tester le téléchargement et l'installation
- [ ] Surveiller les premiers avis
- [ ] Préparer les futures mises à jour

---

**Bonne chance pour votre publication ! 🚀**

L'application My RC Setup est maintenant prête à conquérir la communauté des passionnés de voitures RC !