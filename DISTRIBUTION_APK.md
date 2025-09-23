# 📱 Guide de Distribution APK - My RC Setup

## ✅ Conservation des Données lors des Mises à Jour

**BONNE NOUVELLE : Tes sessions seront TOUJOURS conservées !** 🎉

### 🔒 Pourquoi les données sont sécurisées ?

1. **Base de données persistante** : Room Database stocke les données dans `/data/data/com.myrcsetup.app/databases/rc_database`
2. **ApplicationId stable** : `com.myrcsetup.app` reste identique entre les versions
3. **Signature cohérente** : Tant que tu signes avec la même clé, Android reconnaît l'app comme identique
4. **Backup automatique** : `android:allowBackup="true"` permet la sauvegarde système

### 📊 Schéma de base de données stable

```kotlin
// Version 1 - Structure actuelle (STABLE)
@Entity(tableName = "rc_sessions")
data class RCSession(
    @PrimaryKey val id: String,
    val carName: String,
    val trackName: String,
    val date: LocalDateTime,
    val weather: String,
    val temperature: Int,
    val humidity: Int,
    val windSpeed: Int,
    val trackCondition: String,
    val frontTires: String,
    val rearTires: String,
    val frontSuspension: String,
    val rearSuspension: String,
    val differentials: String,
    val gearing: String,
    val electronics: String,
    val notes: String
)
```

## 🚀 Processus de Distribution APK

### 1. Compilation de l'APK

```bash
# Dans le dossier MyRCSetup
./gradlew assembleRelease

# L'APK sera généré dans :
# app/build/outputs/apk/release/app-release.apk
```

### 2. Signature de l'APK (IMPORTANT)

```bash
# Créer une clé de signature (UNE SEULE FOIS)
keytool -genkey -v -keystore my-release-key.keystore -alias alias_name -keyalg RSA -keysize 2048 -validity 10000

# Signer l'APK
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore my-release-key.keystore app-release.apk alias_name

# Optimiser l'APK
zipalign -v 4 app-release.apk MyRCSetup-v1.4.apk
```

### 3. Distribution aux Amis

```
📁 Partage via :
├── 💾 Clé USB
├── 📧 Email (si < 25MB)
├── ☁️ Google Drive / Dropbox
├── 💬 WhatsApp / Telegram
└── 🔗 GitHub Releases (recommandé)
```

## 🔄 Gestion des Versions Futures

### ✅ Ce qui PRESERVE les données :

- ✅ Augmenter `versionCode` et `versionName`
- ✅ Ajouter de nouveaux champs à la base de données
- ✅ Modifier l'interface utilisateur
- ✅ Corriger des bugs
- ✅ Ajouter de nouvelles fonctionnalités

### ⚠️ Ce qui NÉCESSITE une migration :

- ⚠️ Modifier le type d'un champ existant
- ⚠️ Supprimer un champ de la base de données
- ⚠️ Renommer une table ou un champ

### 📝 Exemple de Migration (si nécessaire)

```kotlin
// Si tu veux ajouter un nouveau champ plus tard
@Database(
    entities = [RCSession::class],
    version = 2, // Incrémenter la version
    exportSchema = false
)
abstract class RCDatabase : RoomDatabase() {
    
    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Ajouter une nouvelle colonne
                database.execSQL("ALTER TABLE rc_sessions ADD COLUMN newField TEXT DEFAULT ''")
            }
        }
        
        fun getDatabase(context: Context): RCDatabase {
            return Room.databaseBuilder(context, RCDatabase::class.java, "rc_database")
                .addMigrations(MIGRATION_1_2) // Ajouter la migration
                .build()
        }
    }
}
```

## 📋 Checklist pour Nouvelle Version

### Avant de compiler :

- [ ] Incrémenter `versionCode` dans `build.gradle.kts`
- [ ] Mettre à jour `versionName` dans `build.gradle.kts`
- [ ] Tester l'application sur un appareil
- [ ] Vérifier que les données existantes sont accessibles

### Compilation :

- [ ] `./gradlew clean`
- [ ] `./gradlew assembleRelease`
- [ ] Signer l'APK avec la MÊME clé
- [ ] Tester l'installation sur un appareil avec l'ancienne version

### Distribution :

- [ ] Renommer l'APK avec le numéro de version
- [ ] Créer une release GitHub avec changelog
- [ ] Partager le lien de téléchargement

## 🛡️ Sécurité des Données

### Sauvegarde Automatique Android

```xml
<!-- AndroidManifest.xml -->
<application android:allowBackup="true">
```

- ✅ Sauvegarde automatique sur Google Drive (si activée)
- ✅ Restauration lors du changement d'appareil
- ✅ Backup local via ADB

### Sauvegarde Manuelle (optionnelle)

```bash
# Exporter les données (nécessite root ou debug)
adb backup -f myrcsetup_backup.ab com.myrcsetup.app

# Restaurer les données
adb restore myrcsetup_backup.ab
```

## 🎯 Recommandations

1. **GARDE LA CLÉ DE SIGNATURE** : Sans elle, impossible de mettre à jour l'app
2. **Teste toujours** : Installe la nouvelle version sur un appareil avec l'ancienne
3. **Versioning cohérent** : Utilise un système de numérotation logique
4. **GitHub Releases** : Utilise les releases GitHub pour un historique propre
5. **Changelog** : Documente toujours les changements

## 📞 Support

Si tes amis ont des problèmes :

1. **Vérifier la version Android** : Minimum API 24 (Android 7.0)
2. **Autoriser les sources inconnues** : Paramètres > Sécurité
3. **Espace de stockage** : Au moins 50MB libres
4. **Désinstaller l'ancienne version** : Seulement si problème de signature

---

**🔥 RÉSUMÉ : Tes sessions RC seront TOUJOURS conservées lors des mises à jour, tant que tu utilises la même clé de signature !**