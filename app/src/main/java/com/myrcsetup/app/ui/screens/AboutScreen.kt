package com.myrcsetup.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.myrcsetup.app.AppConfig
import com.myrcsetup.app.utils.UpdateChecker
import com.myrcsetup.app.utils.UpdateCheckResult
import com.myrcsetup.app.utils.UpdatePreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val coroutineScope = rememberCoroutineScope()
    
    // États pour la gestion des mises à jour
    var isCheckingUpdate by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showNoUpdateDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var updateResult by remember { mutableStateOf<UpdateCheckResult?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    
    // Utilitaires
    val updateChecker = remember { UpdateChecker() }
    val updatePreferences = remember { UpdatePreferences(context) }
    
    // Fonction pour vérifier les mises à jour
    fun checkForUpdates(isManual: Boolean = true) {
        if (isCheckingUpdate) return
        
        isCheckingUpdate = true
        coroutineScope.launch {
            try {
                val result = updateChecker.checkForUpdate(AppConfig.APP_VERSION)
                updateResult = result
                
                when (result) {
                    is UpdateCheckResult.UpdateAvailable -> {
                        // Enregistrer la vérification
                        updatePreferences.setLastCheckTime()
                        
                        // Vérifier si on doit notifier pour cette version
                        if (isManual || updatePreferences.shouldNotifyForVersion(result.release.tag_name)) {
                            showUpdateDialog = true
                            if (!isManual) {
                                updatePreferences.setLastNotifiedVersion(result.release.tag_name)
                            }
                        }
                    }
                    is UpdateCheckResult.NoUpdateAvailable -> {
                        updatePreferences.setLastCheckTime()
                        if (isManual) {
                            showNoUpdateDialog = true
                        }
                    }
                    is UpdateCheckResult.Error -> {
                        errorMessage = result.message
                        if (isManual) {
                            showErrorDialog = true
                        }
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Erreur inattendue: ${e.message}"
                if (isManual) {
                    showErrorDialog = true
                }
            } finally {
                isCheckingUpdate = false
            }
        }
    }
    
    // Vérification automatique au démarrage si nécessaire
    LaunchedEffect(Unit) {
        if (updatePreferences.shouldCheckForUpdate()) {
            checkForUpdates(isManual = false)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("À propos") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Logo et titre principal
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "My RC Setup",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                    
                    Text(
                        text = "Gestion des réglages de voitures RC",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // Informations de version
            InfoCard(
                title = "Informations de version",
                icon = Icons.Default.Info
            ) {
                InfoRow(label = "Version de l'application", value = AppConfig.APP_VERSION)
                InfoRow(label = "Code de version", value = AppConfig.VERSION_CODE.toString())
                InfoRow(label = "Version du format de données", value = AppConfig.EXPORT_FORMAT_VERSION)
                InfoRow(label = "Plateforme", value = "Android")
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Bouton de vérification des mises à jour
                Button(
                    onClick = { checkForUpdates(isManual = true) },
                    enabled = !isCheckingUpdate,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (isCheckingUpdate) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Vérification en cours...")
                    } else {
                        Icon(
                            imageVector = Icons.Default.SystemUpdate,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Vérifier les mises à jour")
                    }
                }
            }
            
            // Informations techniques
            InfoCard(
                title = "Informations techniques",
                icon = Icons.Default.Storage
            ) {
                InfoRow(label = "Base de données", value = "SQLite (Room)")
                InfoRow(label = "Interface utilisateur", value = "Jetpack Compose")
                InfoRow(label = "Langage", value = "Kotlin")
                InfoRow(label = "Architecture", value = "MVVM")
            }
            
            // Contact et développement
            InfoCard(
                title = "Développement et contact",
                icon = Icons.Default.Code
            ) {
                ClickableInfoRow(
                    label = "Code source GitHub",
                    value = "https://github.com/kapoue/MyRCSetup",
                    onClick = { uriHandler.openUri("https://github.com/kapoue/MyRCSetup") }
                )
                
                ClickableInfoRow(
                    label = "Contact email",
                    value = "kapoue@gmail.com",
                    onClick = { uriHandler.openUri("mailto:kapoue@gmail.com") }
                )
                
                InfoRow(label = "Licence", value = "MIT License")
                InfoRow(label = "Développé par", value = "Kapoue")
            }
            
            // Description de l'application
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "À propos de l'application",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Text(
                        text = "My RC Setup est une application Android conçue pour les passionnés de voitures radiocommandées. " +
                                "Elle permet de gérer et sauvegarder tous les réglages techniques de vos sessions d'entraînement : " +
                                "suspension, différentiels, géométrie, transmission, pneus et réglages châssis.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Justify
                    )
                    
                    Text(
                        text = "Fonctionnalités principales :",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Text(
                        text = "• Sauvegarde complète des réglages techniques\n" +
                                "• Historique des sessions d'entraînement\n" +
                                "• Export et import des données en JSON\n" +
                                "• Interface moderne avec thème Racing Joyeuse\n" +
                                "• Fonctionnement 100% hors ligne",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Copyright
            Text(
                text = AppConfig.COPYRIGHT,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
        
        // Dialogues de mise à jour
        
        // Dialogue de mise à jour disponible
        if (showUpdateDialog) {
            val release = (updateResult as? UpdateCheckResult.UpdateAvailable)?.release
            if (release != null) {
                AlertDialog(
                    onDismissRequest = { showUpdateDialog = false },
                    title = {
                        Text(
                            text = "Mise à jour disponible",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Une nouvelle version est disponible :",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Version ${release.tag_name}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Voulez-vous télécharger la mise à jour ?",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showUpdateDialog = false
                                updateChecker.openReleasesPage(context)
                            }
                        ) {
                            Text("Télécharger")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showUpdateDialog = false }
                        ) {
                            Text("Plus tard")
                        }
                    }
                )
            }
        }
        
        // Dialogue aucune mise à jour
        if (showNoUpdateDialog) {
            AlertDialog(
                onDismissRequest = { showNoUpdateDialog = false },
                title = {
                    Text(
                        text = "Application à jour",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "Vous utilisez déjà la dernière version de l'application.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = { showNoUpdateDialog = false }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
        
        // Dialogue d'erreur
        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = {
                    Text(
                        text = "Erreur de vérification",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Impossible de vérifier les mises à jour :",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Vérifiez votre connexion Internet et réessayez.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { showErrorDialog = false }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            content()
        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ClickableInfoRow(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        TextButton(
            onClick = onClick,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.End
            )
        }
    }
}