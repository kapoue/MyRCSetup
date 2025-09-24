package com.myrcsetup.app.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
    
    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            viewModel.clearSaveSuccess()
            onNavigateBack()
        }
    }
    
    // Gestion de la navigation automatique après requestNavigateBack
    LaunchedEffect(uiState.currentSession) {
        if (uiState.currentSession == null && !uiState.showUnsavedChangesDialog) {
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
                    if (session?.id != null && uiState.isEditing) {
                        IconButton(
                            onClick = { viewModel.shareSessionViaQR(session) }
                        ) {
                            Icon(Icons.Default.QrCode, contentDescription = "Partager via QR Code")
                        }
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
            onDismissRequest = { viewModel.cancelExit() },
            title = { Text("Modifications non sauvées") },
            text = { Text("Vous avez des modifications non sauvées. Que souhaitez-vous faire ?") },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.saveAndExit() }
                ) {
                    Text("Sauvegarder et quitter")
                }
            },
            dismissButton = {
                Row {
                    TextButton(
                        onClick = { viewModel.exitWithoutSaving() }
                    ) {
                        Text("Quitter sans sauvegarder")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = { viewModel.cancelExit() }
                    ) {
                        Text("Annuler")
                    }
                }
            }
        )
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
                placeholder = { Text("mm:ss.ms") },
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