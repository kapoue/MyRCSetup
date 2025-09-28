# 📱 Guide de Débogage - Visualisation des Logs Android

## 🔌 Prérequis : Connexion USB

**Oui, vous devez connecter votre smartphone au PC par câble USB** pour voir les logs en temps réel.

## 🛠️ Configuration du Smartphone

### 1. Activer le Mode Développeur
1. Allez dans **Paramètres** → **À propos du téléphone**
2. Tapez **7 fois** sur "Numéro de build" ou "Version du logiciel"
3. Un message "Vous êtes maintenant développeur" apparaît

### 2. Activer le Débogage USB
1. Retournez dans **Paramètres** → **Options pour les développeurs**
2. Activez **"Débogage USB"**
3. Activez **"Rester éveillé"** (optionnel, évite la mise en veille)

### 3. Connecter le Smartphone
1. Connectez le smartphone au PC avec un **câble USB de données**
2. Sur le smartphone, autorisez le **"Débogage USB"** quand la popup apparaît
3. Cochez **"Toujours autoriser depuis cet ordinateur"**

## 🖥️ Méthodes pour Voir les Logs

### Option 1 : Android Studio (Recommandé)
1. Ouvrez **Android Studio**
2. Allez dans **View** → **Tool Windows** → **Logcat**
3. Sélectionnez votre appareil dans la liste déroulante
4. Filtrez par **"MyRCSetup"** ou **"com.myrcsetup.app"**

**Filtres utiles :**
- `tag:MyRCSetup` - Tous les logs de l'app
- `tag:MainActivity` - Logs du démarrage
- `tag:RCDatabase` - Logs de la base de données

### Option 2 : ADB en Ligne de Commande
```bash
# Vérifier que l'appareil est connecté
adb devices

# Voir tous les logs en temps réel
adb logcat

# Filtrer uniquement My RC Setup
adb logcat | grep "MyRCSetup"

# Filtrer par tag spécifique
adb logcat -s "MyRCSetup"

# Sauvegarder les logs dans un fichier
adb logcat > logs_myrcsetup.txt
```

### Option 3 : Applications Tierces
- **Device File Explorer** (dans Android Studio)
- **Vysor** (pour voir l'écran + logs)
- **scrcpy** (open source, gratuit)

## 🔍 Logs Spécifiques à My RC Setup v1.9.2

Avec la version 1.9.2, vous verrez ces logs au démarrage :

```
MyRCSetup: === APPLICATION STARTED - VERSION 1.9.2 (Build 40) ===
MyRCSetup: 🔄 Initialisation de la base de données...
MyRCSetup: ✅ Base de données initialisée
MyRCSetup: 🔄 Migration de la base de données...
MyRCSetup: ✅ Migration 1→2 réussie : Table 'notes' créée
MyRCSetup: 🔄 Création des repositories...
MyRCSetup: ✅ Repositories créés
MyRCSetup: 🔄 Création des ViewModelFactory...
MyRCSetup: ✅ ViewModelFactory créés
MyRCSetup: 🔄 Initialisation de l'interface utilisateur...
MyRCSetup: ✅ ViewModels créés
MyRCSetup: 🔄 Initialisation de la navigation...
MyRCSetup: ✅ Navigation initialisée
MyRCSetup: 🎉 APPLICATION DÉMARRÉE AVEC SUCCÈS!
```

## ❌ En Cas de Crash

Si l'application crash, vous verrez :
```
AndroidRuntime: FATAL EXCEPTION: main
AndroidRuntime: Process: com.myrcsetup.app, PID: 12345
AndroidRuntime: java.lang.RuntimeException: ...
```

## 🚨 Dépannage Connexion USB

### Problème : Appareil Non Détecté
1. **Vérifiez le câble** : Utilisez un câble de données (pas seulement charge)
2. **Changez de port USB** sur le PC
3. **Redémarrez ADB** :
   ```bash
   adb kill-server
   adb start-server
   adb devices
   ```

### Problème : Autorisation Refusée
1. Débranchez et rebranchez le câble
2. Révoqué les autorisations : **Options développeur** → **Révoquer autorisations débogage USB**
3. Reconnectez et autorisez à nouveau

### Problème : Drivers Windows
1. Téléchargez les **drivers USB** de votre fabricant (Samsung, Xiaomi, etc.)
2. Ou installez les **Google USB Drivers** via Android Studio

## 📋 Checklist de Débogage

- [ ] Mode développeur activé
- [ ] Débogage USB activé  
- [ ] Câble USB de données connecté
- [ ] Autorisation accordée sur le smartphone
- [ ] `adb devices` montre l'appareil
- [ ] Android Studio/Logcat ouvert
- [ ] Filtres configurés pour "MyRCSetup"

## 🎯 Test Rapide

Pour tester que tout fonctionne :

1. Connectez le smartphone
2. Ouvrez Android Studio → Logcat
3. Lancez My RC Setup
4. Vous devriez voir : `=== APPLICATION STARTED - VERSION 1.9.2 ===`

## 📞 Support

Si vous ne voyez toujours pas les logs :
1. Vérifiez que l'app est bien installée depuis l'APK v1.9.2
2. Redémarrez l'application complètement
3. Vérifiez les filtres dans Logcat
4. Essayez `adb logcat` en ligne de commande

---

**💡 Astuce :** Gardez Logcat ouvert pendant que vous utilisez l'application pour voir tous les logs en temps réel !