# Guide de Debugging - Double-clic sur les sessions

## Étapes pour récupérer les logs Android

### 1. Préparer le smartphone
1. **Activer le mode développeur** :
   - Aller dans Paramètres → À propos du téléphone
   - Appuyer 7 fois sur "Numéro de build"
   - Le mode développeur est maintenant activé

2. **Activer le débogage USB** :
   - Aller dans Paramètres → Options pour les développeurs
   - Activer "Débogage USB"

### 2. Connecter au PC
1. **Connecter le smartphone au PC** avec un câble USB
2. **Autoriser le débogage** quand la popup apparaît sur le téléphone
3. **Vérifier la connexion** :
   - Ouvrir un terminal/invite de commande
   - Taper : `adb devices`
   - Ton téléphone doit apparaître dans la liste

### 3. Récupérer les logs
1. **Ouvrir PowerShell ou CMD** et taper :
   ```powershell
   adb logcat | Select-String "SessionCard"
   ```
   
   **OU** si tu préfères voir tous les logs et chercher manuellement :
   ```cmd
   adb logcat
   ```
   (puis chercher visuellement les lignes contenant "SessionCard")

2. **Lancer l'app My RC Setup** sur le téléphone

3. **Tester les clics** sur les boutons crayon (edit) des sessions

4. **Observer les logs** dans le terminal - tu devrais voir :
   ```
   D/SessionCard: Edit button clicked for session: [nom] (ID: [id])
   ```

### 3bis. Alternative Windows simple
Si les commandes ci-dessus ne marchent pas, utilise cette méthode :
1. **Ouvrir CMD** et taper simplement :
   ```cmd
   adb logcat
   ```
2. **Lancer l'app** et **tester les clics**
3. **Chercher visuellement** dans le flux de logs les lignes qui contiennent "SessionCard"
4. **Copier/coller** ces lignes ici

### 4. Que chercher dans les logs
- **Si tu vois 1 log par clic** → Le problème vient de la navigation
- **Si tu vois 2 logs par clic** → Il y a un double événement
- **Si tu ne vois aucun log** → Le clic n'est pas détecté

### 5. Alternative simple
Si tu n'arrives pas à configurer ADB, dis-moi juste :
- Combien de fois tu dois cliquer pour que ça marche ?
- Est-ce que ça marche parfois du premier coup ?
- Est-ce que c'est pareil pour tous les boutons crayon ?

## Prochaines étapes
Une fois qu'on aura identifié le problème exact, on passera au BATCH 3 v1.7.4 pour corriger le bouton QR Code.