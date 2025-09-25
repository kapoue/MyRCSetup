package com.myrcsetup.app.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myrcsetup.app.data.entity.RCSession
import com.myrcsetup.app.ui.viewmodel.RCSessionViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SessionFormScreen(
    viewModel: RCSessionViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val session = uiState.currentSession
    val context = androidx.compose.ui.platform.LocalContext.current
    
    // Gestion du bouton retour Android
    androidx.activity.compose.BackHandler {
        viewModel.requestNavigateBack()
    }
    
    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            android.util.Log.d("Navigation", "saveSuccess = true, navigating back and clearing currentSession")
            viewModel.clearSaveSuccess()
            // Nettoyer la session avant de naviguer pour éviter le conflit avec l'autre LaunchedEffect
            viewModel.clearCurrentSession()
            onNavigateBack()
        }
    }
    
    // Gestion automatique du partage QR Code
    LaunchedEffect(uiState.shareIntent) {
        uiState.shareIntent?.let { intent ->
            android.util.Log.d("QRCode", "Launching share intent from UI")
            try {
                context.startActivity(intent)
                viewModel.clearShareIntent()
            } catch (e: Exception) {
                android.util.Log.e("QRCode", "Failed to launch share intent: ${e.message}")
            }
        }
    }
    
    // Gestion de la navigation automatique après requestNavigateBack
    LaunchedEffect(uiState.currentSession) {
        if (uiState.currentSession == null && !uiState.showUnsavedChangesDialog && !uiState.saveSuccess) {
            android.util.Log.d("Navigation", "currentSession = null, navigating back (not from save)")
            onNavigateBack()
        }
    }

    if (session == null) {
        // État de chargement ou erreur
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (uiState.isEditing)
                            "Modifier Session"
                        else
                            "Nouvelle Session"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.requestNavigateBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    // Bouton Sauvegarder (toujours visible)
                    IconButton(
                        onClick = { viewModel.saveSession() }
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Sauvegarder")
                    }
                    
                    // Bouton QR Code (visible uniquement si session sauvegardée)
                    if (session.id != 0L) {
                        android.util.Log.d("QRButton", "QR Code button is visible for session ID: ${session.id}")
                        IconButton(
                            onClick = {
                                android.util.Log.d("QRButton", "QR Code button clicked for session: ${session.carName} (ID: ${session.id})")
                                viewModel.generateQRCodeForSession(session)
                            }
                        ) {
                            Icon(Icons.Default.QrCode, contentDescription = "Afficher QR Code")
                        }
                    } else {
                        android.util.Log.d("QRButton", "QR Code button is hidden - session ID: ${session.id}")
                    }
                }
            )
        }
    ) { paddingValues ->
        SessionForm(
            session = session,
            onSessionUpdate = viewModel::updateCurrentSession,
            onSaveSession = { viewModel.saveSession() },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }

    // Affichage des erreurs
    uiState.errorMessage?.let { error ->
        LaunchedEffect(error) {
            // Ici on pourrait afficher un Snackbar
            viewModel.clearError()
        }
    }

    // Dialogue de confirmation pour modifications non sauvées
    if (uiState.showUnsavedChangesDialog) {
        AlertDialog(
            onDismissRequest = {
                android.util.Log.d("UnsavedDialog", "Dialog dismissed")
                viewModel.cancelExit()
            },
            title = { Text("Modifications non sauvées") },
            text = { Text("Vous avez des modifications non sauvées. Que souhaitez-vous faire ?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        android.util.Log.d("UnsavedDialog", "Save and exit clicked")
                        viewModel.saveAndExit()
                    }
                ) {
                    Text("Sauvegarder et quitter")
                }
            },
            dismissButton = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TextButton(
                        onClick = {
                            android.util.Log.d("UnsavedDialog", "Exit without saving clicked")
                            viewModel.exitWithoutSaving()
                        }
                    ) {
                        Text("Quitter sans sauvegarder")
                    }
                    TextButton(
                        onClick = {
                            android.util.Log.d("UnsavedDialog", "Cancel clicked")
                            viewModel.cancelExit()
                        }
                    ) {
                        Text("Annuler")
                    }
                }
            }
        )
    }

    // Dialogue QR Code
    if (uiState.showQRCodeDialog && uiState.qrCodeBitmap != null) {
        Dialog(
            onDismissRequest = { viewModel.closeQRCodeDialog() }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "QR Code - ${session.carName}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Image(
                        bitmap = uiState.qrCodeBitmap!!.asImageBitmap(),
                        contentDescription = "QR Code de la session",
                        modifier = Modifier.size(300.dp)
                    )
                    
                    Text(
                        text = "Scannez ce QR code avec un autre appareil pour importer cette session",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Button(
                        onClick = { viewModel.closeQRCodeDialog() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Fermer")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SessionForm(
    session: RCSession,
    onSessionUpdate: (RCSession) -> Unit,
    onSaveSession: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    
    // Helper function for keyboard navigation
    fun createKeyboardActions(isLastField: Boolean = false, isCommentField: Boolean = false): KeyboardActions {
        return KeyboardActions(
            onNext = {
                if (!isLastField) {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            },
            onDone = {
                if (isLastField) {
                    onSaveSession()
                    focusManager.clearFocus()
                } else if (isCommentField) {
                    // For comment field, allow new line on Enter
                } else {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            }
        )
    }
    
    // Helper function for focus handling with auto-scroll
    fun createFocusModifier(): Modifier {
        return Modifier.onFocusChanged { focusState ->
            if (focusState.isFocused) {
                coroutineScope.launch {
                    bringIntoViewRequester.bringIntoView()
                }
            }
        }
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .bringIntoViewRequester(bringIntoViewRequester),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Informations de base
        SectionCard(
            title = "Informations générales",
            icon = Icons.Default.DirectionsCar
        ) {
            OutlinedTextField(
                value = session.carName,
                onValueChange = { onSessionUpdate(session.copy(carName = it)) },
                label = { Text("Nom de la voiture") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
            
            OutlinedTextField(
                value = session.trackName,
                onValueChange = { onSessionUpdate(session.copy(trackName = it)) },
                label = { Text("Nom du circuit") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
            
            // Date et heure masquée - automatique lors de la sauvegarde
            
            OutlinedTextField(
                value = session.bestLapTime,
                onValueChange = { onSessionUpdate(session.copy(bestLapTime = it)) },
                label = { Text("Meilleur temps au tour") },
                placeholder = { Text("ex: 1:23.45") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
            
            OutlinedTextField(
                value = session.comments,
                onValueChange = { onSessionUpdate(session.copy(comments = it)) },
                label = { Text("Commentaires") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = createKeyboardActions(isCommentField = true),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier()),
                minLines = 3,
                maxLines = 5
            )
        }

        // Suspension
        SectionCard(
            title = "Suspension",
            icon = Icons.Default.Build
        ) {
            OutlinedTextField(
                value = session.frontSprings,
                onValueChange = { onSessionUpdate(session.copy(frontSprings = it)) },
                label = { Text("Ressorts avant") },
                placeholder = { Text("Blanc, Bleu, Rouge...") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
            
            OutlinedTextField(
                value = session.rearSprings,
                onValueChange = { onSessionUpdate(session.copy(rearSprings = it)) },
                label = { Text("Ressorts arrière") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
            
            OutlinedTextField(
                value = session.frontShockOil?.toString() ?: "",
                onValueChange = {
                    val value = it.toDoubleOrNull()
                    onSessionUpdate(session.copy(frontShockOil = value))
                },
                label = { Text("Huile amortisseur avant") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
            
            OutlinedTextField(
                value = session.rearShockOil?.toString() ?: "",
                onValueChange = {
                    val value = it.toDoubleOrNull()
                    onSessionUpdate(session.copy(rearShockOil = value))
                },
                label = { Text("Huile amortisseur arrière") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
            
            OutlinedTextField(
                value = session.shockPosition,
                onValueChange = { onSessionUpdate(session.copy(shockPosition = it)) },
                label = { Text("Position amortisseurs") },
                placeholder = { Text("Trou 1, Trou 2...") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
        }

        // Différentiels
        SectionCard(
            title = "Différentiels",
            icon = Icons.Default.Settings
        ) {
            OutlinedTextField(
                value = session.frontDiffOil?.toString() ?: "",
                onValueChange = {
                    val value = it.toDoubleOrNull()
                    onSessionUpdate(session.copy(frontDiffOil = value))
                },
                label = { Text("Huile différentiel avant") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
            
            OutlinedTextField(
                value = session.rearDiffOil?.toString() ?: "",
                onValueChange = {
                    val value = it.toDoubleOrNull()
                    onSessionUpdate(session.copy(rearDiffOil = value))
                },
                label = { Text("Huile différentiel arrière") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
            
            OutlinedTextField(
                value = session.centerDiffOil?.toString() ?: "",
                onValueChange = {
                    val value = it.toDoubleOrNull()
                    onSessionUpdate(session.copy(centerDiffOil = value))
                },
                label = { Text("Huile différentiel central") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
        }

        // Géométrie
        SectionCard(
            title = "Géométrie",
            icon = Icons.Default.Architecture
        ) {
            OutlinedTextField(
                value = session.frontCamber?.toString() ?: "",
                onValueChange = {
                    val value = it.toDoubleOrNull()
                    onSessionUpdate(session.copy(frontCamber = value))
                },
                label = { Text("Carrossage avant") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
            
            OutlinedTextField(
                value = session.rearCamber?.toString() ?: "",
                onValueChange = {
                    val value = it.toDoubleOrNull()
                    onSessionUpdate(session.copy(rearCamber = value))
                },
                label = { Text("Carrossage arrière") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
            
            OutlinedTextField(
                value = session.frontToe?.toString() ?: "",
                onValueChange = {
                    val value = it.toDoubleOrNull()
                    onSessionUpdate(session.copy(frontToe = value))
                },
                label = { Text("Pincement avant") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
            
            OutlinedTextField(
                value = session.rearToe?.toString() ?: "",
                onValueChange = {
                    val value = it.toDoubleOrNull()
                    onSessionUpdate(session.copy(rearToe = value))
                },
                label = { Text("Pincement arrière") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
            
            OutlinedTextField(
                value = session.caster?.toString() ?: "",
                onValueChange = {
                    val value = it.toDoubleOrNull()
                    onSessionUpdate(session.copy(caster = value))
                },
                label = { Text("Chasse") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
        }

        // Transmission
        SectionCard(
            title = "Transmission",
            icon = Icons.Default.Link
        ) {
            OutlinedTextField(
                value = session.pinion?.toString() ?: "",
                onValueChange = {
                    val value = it.toIntOrNull()
                    onSessionUpdate(session.copy(pinion = value))
                },
                label = { Text("Pignon moteur") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
            
            OutlinedTextField(
                value = session.spurGear?.toString() ?: "",
                onValueChange = {
                    val value = it.toIntOrNull()
                    onSessionUpdate(session.copy(spurGear = value))
                },
                label = { Text("Couronne") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
        }

        // Pneus et adhérence
        SectionCard(
            title = "Pneus et Adhérence",
            icon = Icons.Default.Circle
        ) {
            OutlinedTextField(
                value = session.frontTires,
                onValueChange = { onSessionUpdate(session.copy(frontTires = it)) },
                label = { Text("Pneus avant") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
            
            OutlinedTextField(
                value = session.rearTires,
                onValueChange = { onSessionUpdate(session.copy(rearTires = it)) },
                label = { Text("Pneus arrière") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
            
            OutlinedTextField(
                value = session.tireFoam,
                onValueChange = { onSessionUpdate(session.copy(tireFoam = it)) },
                label = { Text("Mousse pneus") },
                placeholder = { Text("Soft, Medium, Hard") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
            
            OutlinedTextField(
                value = session.tractionAdditive,
                onValueChange = { onSessionUpdate(session.copy(tractionAdditive = it)) },
                label = { Text("Additif d'adhérence") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
        }

        // Réglages châssis
        SectionCard(
            title = "Réglages Châssis",
            icon = Icons.Default.Construction
        ) {
            OutlinedTextField(
                value = session.chassisStiffness,
                onValueChange = { onSessionUpdate(session.copy(chassisStiffness = it)) },
                label = { Text("Rigidité châssis") },
                placeholder = { Text("Standard, +1 carbon brace") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
            
            OutlinedTextField(
                value = session.frontRideHeight?.toString() ?: "",
                onValueChange = {
                    val value = it.toDoubleOrNull()
                    onSessionUpdate(session.copy(frontRideHeight = value))
                },
                label = { Text("Hauteur caisse avant") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
            
            OutlinedTextField(
                value = session.rearRideHeight?.toString() ?: "",
                onValueChange = {
                    val value = it.toDoubleOrNull()
                    onSessionUpdate(session.copy(rearRideHeight = value))
                },
                label = { Text("Hauteur caisse arrière") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
            
            OutlinedTextField(
                value = session.frontAntiRoll?.toString() ?: "",
                onValueChange = {
                    val value = it.toDoubleOrNull()
                    onSessionUpdate(session.copy(frontAntiRoll = value))
                },
                label = { Text("Anti-roulis avant") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = createKeyboardActions(),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
            
            // DERNIER CHAMP - Déclenche la sauvegarde
            OutlinedTextField(
                value = session.rearAntiRoll?.toString() ?: "",
                onValueChange = {
                    val value = it.toDoubleOrNull()
                    onSessionUpdate(session.copy(rearAntiRoll = value))
                },
                label = { Text("Anti-roulis arrière") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = createKeyboardActions(isLastField = true),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(createFocusModifier())
            )
        }

        // Espacement en bas pour le FAB
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun SectionCard(
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
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
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