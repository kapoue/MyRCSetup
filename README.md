# My RC Setup 🏁

Une application Android moderne pour gérer les réglages de voitures RC pendant les sessions d'entraînement.

## 📱 Fonctionnalités

- **Gestion complète des réglages** : Moteur, châssis, pneus, amortisseurs
- **Historique des sessions** : Sauvegarde automatique avec date et heure
- **Interface moderne** : Material Design 3 avec Jetpack Compose
- **Calculs automatiques** : Rapport final calculé automatiquement
- **Base de données locale** : Stockage sécurisé avec Room Database

## 🛠️ Technologies Utilisées

- **Kotlin** - Langage de programmation
- **Jetpack Compose** - Interface utilisateur moderne
- **Room Database** - Base de données locale SQLite
- **Material Design 3** - Design system Google
- **MVVM Architecture** - Architecture recommandée Android
- **Navigation Component** - Navigation entre écrans

## 📋 Réglages Supportés

### Moteur
- Pignon moteur et couronne
- Rapport de transmission
- Type de moteur (brushed/brushless)

### Châssis
- Empattement et voie
- Hauteur de caisse
- Anti-roulis avant/arrière

### Pneus
- Compound avant/arrière
- Pression des pneus
- Marque et modèle

### Amortisseurs
- Huile amortisseurs (viscosité)
- Ressorts avant/arrière
- Réglages compression/détente

## 🚀 Installation

### 📱 Distribution APK (Recommandé)
1. Téléchargez le fichier APK depuis les [Releases GitHub](https://github.com/kapoue/MyRCSetup/releases)
2. Activez "Sources inconnues" dans les paramètres Android
3. Installez l'APK sur votre appareil
4. **✅ Vos sessions seront conservées lors des mises à jour !**

### 🔧 Compilation depuis les sources

#### Windows
```bash
git clone https://github.com/kapoue/MyRCSetup.git
cd MyRCSetup
build_apk.bat
```

#### Linux/Mac
```bash
git clone https://github.com/kapoue/MyRCSetup.git
cd MyRCSetup
chmod +x build_apk.sh
./build_apk.sh
```

#### Manuel
```bash
./gradlew assembleRelease
```

📖 **Guide complet** : Consultez [DISTRIBUTION_APK.md](DISTRIBUTION_APK.md) pour tous les détails

## 📖 Utilisation

1. **Créer une session** : Appuyez sur le bouton "+" 
2. **Remplir les réglages** : Saisissez vos paramètres RC
3. **Sauvegarder** : L'application calcule automatiquement le rapport final
4. **Consulter l'historique** : Retrouvez toutes vos sessions précédentes

## 🤝 Contribution

Les contributions sont les bienvenues ! N'hésitez pas à :
- Signaler des bugs via les [Issues](https://github.com/votrenom/MyRCSetup/issues)
- Proposer des améliorations
- Soumettre des Pull Requests

## 📄 Licence

Ce projet est sous licence GPL-3.0 - voir le fichier [LICENSE](LICENSE) pour plus de détails.

## 🏁 Pour la Communauté RC

Développé par un passionné pour la communauté RC française. 
Partagez vos réglages, améliorez vos performances !

---

**Bon run ! 🏎️💨**