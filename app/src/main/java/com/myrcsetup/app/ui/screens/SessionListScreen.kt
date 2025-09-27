package com.myrcsetup.app.ui.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myrcsetup.app.data.entity.RCSession
import com.myrcsetup.app.ui.viewmodel.RCSessionViewModel
import com.myrcsetup.app.utils.QRCodeScannerComposable
import kotlinx.coroutines.launch
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionListScreen(
    viewModel: RCSessionViewModel,
    onNavigateToNewSession: () -> Unit,
    onNavigateToEditSession: (Long) -> Unit,
    onNavigateToAbout: () -> Unit = {},
    onNavigateToNotes: () -> Unit = {},
    onExportData: () -> Unit = {},
    onImportData: () -> Unit = {}
) {
    val context = LocalContext.current
    val sessions by viewModel.sessions.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf<RCSession?>(null) }
    var showDropdownMenu by remember { mutableStateOf(false) }
    
    // État pour le scroll et highlight
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    
    // Launcher pour l'import de fichier
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { viewModel.importDataFromUri(context, it) }
    }
    
    // Gestion des Intent de partage/import
    LaunchedEffect(uiState.shareIntent) {
        uiState.shareIntent?.let { intent ->
            context.startActivity(intent)
            viewModel.clearShareIntent()
        }
    }
    
    // Messages de succès/erreur avec Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    
    uiState.errorMessage?.let { error ->
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Long
            )
            viewModel.clearError()
        }
    }
    
    if (uiState.importSuccess) {
        LaunchedEffect(uiState.importSuccess) {
            snackbarHostState.showSnackbar(
                message = "Import réussi !",
                duration = SnackbarDuration.Short
            )
            viewModel.clearImportSuccess()
        }
    }
    
    // Gestion du scroll automatique vers la session
    LaunchedEffect(uiState.scrollToSessionId, sessions.size) {
        uiState.scrollToSessionId?.let { sessionId ->
            // Attendre un peu que la liste soit mise à jour
            kotlinx.coroutines.delay(100)
            
            val sessionIndex = sessions.indexOfFirst { it.id == sessionId }
            android.util.Log.d("Scroll", "Looking for session ID $sessionId, found at index $sessionIndex, total sessions: ${sessions.size}")
            
            if (sessionIndex != -1) {
                coroutineScope.launch {
                    android.util.Log.d("Scroll", "Scrolling to session at index $sessionIndex")
                    if (sessionIndex == 0) {
                        // Nouvelle session en haut - scroll vers le tout début
                        listState.animateScrollToItem(0)
                        android.util.Log.d("Scroll", "Scrolled to top (index 0)")
                    } else {
                        // Session modifiée, scroll vers sa position
                        listState.animateScrollToItem(sessionIndex)
                        android.util.Log.d("Scroll", "Scrolled to index $sessionIndex")
                    }
                }
            } else {
                android.util.Log.w("Scroll", "Session ID $sessionId not found in list")
            }
            // Nettoyer seulement le scroll, pas le highlight
            viewModel.clearScrollToSessionId()
        }
    }

    // Interface de scan QR Code - directement la caméra
    if (uiState.isQRScanningActive) {
        val context = LocalContext.current
        var hasPermission by remember {
            mutableStateOf(com.myrcsetup.app.utils.QRCodeScanner.hasCameraPermission(context))
        }
        
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            hasPermission = isGranted
            if (!isGranted) {
                viewModel.stopQRCodeScanning()
            }
        }
        
        val scanLauncher = rememberLauncherForActivityResult(
            contract = com.journeyapps.barcodescanner.ScanContract()
        ) { result: com.journeyapps.barcodescanner.ScanIntentResult ->
            if (result.contents != null) {
                viewModel.importSessionFromQR(result.contents)
            }
            viewModel.stopQRCodeScanning()
        }
        
        LaunchedEffect(Unit) {
            if (hasPermission) {
                val options = com.myrcsetup.app.utils.QRCodeScanner.createScanOptions()
                scanLauncher.launch(options)
            } else {
                permissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
        
        // Interface minimale pendant le scan
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CircularProgressIndicator()
                Text(
                    text = if (hasPermission) "Ouverture de la caméra..." else "Demande d'autorisation...",
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(
                    onClick = { viewModel.stopQRCodeScanning() }
                ) {
                    Text("Annuler")
                }
            }
        }
        return
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("My RC Setup") },
                actions = {
                    Box {
                        IconButton(onClick = { showDropdownMenu = true }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        DropdownMenu(
                            expanded = showDropdownMenu,
                            onDismissRequest = { showDropdownMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Exporter les données") },
                                onClick = {
                                    showDropdownMenu = false
                                    viewModel.exportDataToJson(context)
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.FileDownload,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                            
                            DropdownMenuItem(
                                text = { Text("Importer les données") },
                                onClick = {
                                    showDropdownMenu = false
                                    importLauncher.launch(arrayOf("application/json", "text/plain"))
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.FileUpload,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            )
                            
                            DropdownMenuItem(
                                text = { Text("Importer via QR Code") },
                                onClick = {
                                    showDropdownMenu = false
                                    viewModel.startQRCodeScanning()
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.QrCode,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.tertiary
                                    )
                                }
                            )
                            
                            DropdownMenuItem(
                                text = { Text("Bloc-notes") },
                                onClick = {
                                    showDropdownMenu = false
                                    onNavigateToNotes()
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Notes,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                            
                            DropdownMenuItem(
                                text = { Text("À propos") },
                                onClick = {
                                    showDropdownMenu = false
                                    onNavigateToAbout()
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Info,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.tertiary
                                    )
                                }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToNewSession,
                containerColor = MaterialTheme.colorScheme.primary,  // Orange racing
                contentColor = MaterialTheme.colorScheme.onPrimary   // Blanc sur orange
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Nouvelle Session",
                    tint = MaterialTheme.colorScheme.onPrimary  // Force la couleur blanche
                )
            }
        }
    ) { paddingValues ->
        if (sessions.isEmpty()) {
            EmptyStateContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                onCreateFirstSession = onNavigateToNewSession
            )
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)  // Plus d'espace entre les cards
            ) {
                items(sessions, key = { it.id }) { session ->
                    val isHighlighted = uiState.highlightedSessionId == session.id
                    if (isHighlighted) {
                        Log.d("Highlight", "Session ${session.id} (${session.carName}) is highlighted!")
                    }
                    SessionCard(
                        session = session,
                        onEdit = { onNavigateToEditSession(session.id) },
                        onDelete = { showDeleteDialog = session },
                        isHighlighted = isHighlighted
                    )
                }
            }
        }
    }

    // Dialog de confirmation de suppression
    showDeleteDialog?.let { session ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Confirmer la suppression") },
            text = { Text("Êtes-vous sûr de vouloir supprimer cette session ?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteSession(session)
                        showDeleteDialog = null
                    }
                ) {
                    Text("Oui")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = null }
                ) {
                    Text("Non")
                }
            }
        )
    }
}

@Composable
fun EmptyStateContent(
    modifier: Modifier = Modifier,
    onCreateFirstSession: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Aucune session enregistrée",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Créez votre première session d'entraînement",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onCreateFirstSession) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Créer une session")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionCard(
    session: RCSession,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    isHighlighted: Boolean = false
) {
    // Animation de couleur pour le highlight
    val highlightColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f) // Vert lime avec transparence
    val normalColor = MaterialTheme.colorScheme.surface
    
    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isHighlighted) highlightColor else normalColor,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = 0
        ),
        label = "highlight_animation"
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)  // Bordure bleu électrique subtile
        ),
        colors = CardDefaults.cardColors(
            containerColor = animatedBackgroundColor,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)  // Plus de padding pour plus d'espace
        ) {
            // Header avec icône de voiture
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = session.carName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = session.trackName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = session.dateTime.toJavaLocalDateTime()
                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.getDefault())),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(48.dp)  // Taille fixe pour éviter les problèmes de clic
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Modifier",
                            tint = MaterialTheme.colorScheme.secondary,  // Bleu électrique pour l'édition
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(48.dp)  // Taille fixe pour éviter les problèmes de clic
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Supprimer",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            
            if (session.bestLapTime.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                // Divider subtil
                Divider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Meilleur temps: ${session.bestLapTime}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary  // Orange racing
                )
            }
            
            if (session.comments.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = session.comments,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}