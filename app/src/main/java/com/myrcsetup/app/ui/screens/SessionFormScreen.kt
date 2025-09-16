package com.myrcsetup.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myrcsetup.app.data.entity.RCSession
import com.myrcsetup.app.ui.viewmodel.RCSessionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
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
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.saveSession() }
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Sauvegarder")
                    }
                }
            )
        }
    ) { paddingValues ->
        SessionForm(
            session = session,
            onSessionUpdate = viewModel::updateCurrentSession,
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
}

@Composable
fun SessionForm(
    session: RCSession,
    onSessionUpdate: (RCSession) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Informations de base
        SectionCard(title = "Informations générales") {
            OutlinedTextField(
                value = session.carName,
                onValueChange = { onSessionUpdate(session.copy(carName = it)) },
                label = { Text("Nom de la voiture") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = session.trackName,
                onValueChange = { onSessionUpdate(session.copy(trackName = it)) },
                label = { Text("Nom du circuit") },
                modifier = Modifier.fillMaxWidth()
            )
            
            // Date et heure (lecture seule)
            OutlinedTextField(
                value = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date()),
                onValueChange = { },
                label = { Text("Date et heure") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            
            OutlinedTextField(
                value = session.bestLapTime,
                onValueChange = { onSessionUpdate(session.copy(bestLapTime = it)) },
                label = { Text("Meilleur temps au tour") },
                placeholder = { Text("mm:ss.ms") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = session.comments,
                onValueChange = { onSessionUpdate(session.copy(comments = it)) },
                label = { Text("Commentaires") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )
        }

        // Suspension
        SectionCard(title = "Suspension") {
            OutlinedTextField(
                value = session.frontSprings,
                onValueChange = { onSessionUpdate(session.copy(frontSprings = it)) },
                label = { Text("Ressorts avant") },
                placeholder = { Text("Blanc, Bleu, Rouge...") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = session.rearSprings,
                onValueChange = { onSessionUpdate(session.copy(rearSprings = it)) },
                label = { Text("Ressorts arrière") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = session.frontShockOil?.toString() ?: "",
                    onValueChange = { 
                        val value = it.toDoubleOrNull()
                        onSessionUpdate(session.copy(frontShockOil = value))
                    },
                    label = { Text("Huile amortisseur avant") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                
                OutlinedTextField(
                    value = session.rearShockOil?.toString() ?: "",
                    onValueChange = { 
                        val value = it.toDoubleOrNull()
                        onSessionUpdate(session.copy(rearShockOil = value))
                    },
                    label = { Text("Huile amortisseur arrière") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
            }
            
            OutlinedTextField(
                value = session.shockPosition,
                onValueChange = { onSessionUpdate(session.copy(shockPosition = it)) },
                label = { Text("Position amortisseurs") },
                placeholder = { Text("Trou 1, Trou 2...") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Différentiels
        SectionCard(title = "Différentiels") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = session.frontDiffOil?.toString() ?: "",
                    onValueChange = { 
                        val value = it.toDoubleOrNull()
                        onSessionUpdate(session.copy(frontDiffOil = value))
                    },
                    label = { Text("Huile différentiel avant") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                
                OutlinedTextField(
                    value = session.rearDiffOil?.toString() ?: "",
                    onValueChange = { 
                        val value = it.toDoubleOrNull()
                        onSessionUpdate(session.copy(rearDiffOil = value))
                    },
                    label = { Text("Huile différentiel arrière") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
            }
            
            OutlinedTextField(
                value = session.centerDiffOil?.toString() ?: "",
                onValueChange = { 
                    val value = it.toDoubleOrNull()
                    onSessionUpdate(session.copy(centerDiffOil = value))
                },
                label = { Text("Huile différentiel central") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Géométrie
        SectionCard(title = "Géométrie") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = session.frontCamber?.toString() ?: "",
                    onValueChange = { 
                        val value = it.toDoubleOrNull()
                        onSessionUpdate(session.copy(frontCamber = value))
                    },
                    label = { Text("Carrossage avant") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                
                OutlinedTextField(
                    value = session.rearCamber?.toString() ?: "",
                    onValueChange = { 
                        val value = it.toDoubleOrNull()
                        onSessionUpdate(session.copy(rearCamber = value))
                    },
                    label = { Text("Carrossage arrière") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = session.frontToe,
                    onValueChange = { onSessionUpdate(session.copy(frontToe = it)) },
                    label = { Text("Pincement avant") },
                    placeholder = { Text("1mm toe-in") },
                    modifier = Modifier.weight(1f)
                )
                
                OutlinedTextField(
                    value = session.rearToe,
                    onValueChange = { onSessionUpdate(session.copy(rearToe = it)) },
                    label = { Text("Pincement arrière") },
                    modifier = Modifier.weight(1f)
                )
            }
            
            OutlinedTextField(
                value = session.caster?.toString() ?: "",
                onValueChange = { 
                    val value = it.toDoubleOrNull()
                    onSessionUpdate(session.copy(caster = value))
                },
                label = { Text("Chasse") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Transmission
        SectionCard(title = "Transmission") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = session.pinion?.toString() ?: "",
                    onValueChange = { 
                        val value = it.toIntOrNull()
                        onSessionUpdate(session.copy(pinion = value))
                    },
                    label = { Text("Pignon moteur") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                
                OutlinedTextField(
                    value = session.spurGear?.toString() ?: "",
                    onValueChange = { 
                        val value = it.toIntOrNull()
                        onSessionUpdate(session.copy(spurGear = value))
                    },
                    label = { Text("Couronne") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Rapport final calculé automatiquement
            session.calculateFinalRatio()?.let { ratio ->
                OutlinedTextField(
                    value = String.format("%.2f", ratio),
                    onValueChange = { },
                    label = { Text("Rapport final") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
            }
        }

        // Pneus et adhérence
        SectionCard(title = "Pneus et Adhérence") {
            OutlinedTextField(
                value = session.frontTires,
                onValueChange = { onSessionUpdate(session.copy(frontTires = it)) },
                label = { Text("Pneus avant") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = session.rearTires,
                onValueChange = { onSessionUpdate(session.copy(rearTires = it)) },
                label = { Text("Pneus arrière") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = session.tireFoam,
                onValueChange = { onSessionUpdate(session.copy(tireFoam = it)) },
                label = { Text("Mousse pneus") },
                placeholder = { Text("Soft, Medium, Hard") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = session.tractionAdditive,
                onValueChange = { onSessionUpdate(session.copy(tractionAdditive = it)) },
                label = { Text("Additif d'adhérence") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Réglages châssis
        SectionCard(title = "Réglages Châssis") {
            OutlinedTextField(
                value = session.chassisStiffness,
                onValueChange = { onSessionUpdate(session.copy(chassisStiffness = it)) },
                label = { Text("Rigidité châssis") },
                placeholder = { Text("Standard, +1 carbon brace") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = session.frontRideHeight?.toString() ?: "",
                    onValueChange = { 
                        val value = it.toDoubleOrNull()
                        onSessionUpdate(session.copy(frontRideHeight = value))
                    },
                    label = { Text("Hauteur caisse avant") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                
                OutlinedTextField(
                    value = session.rearRideHeight?.toString() ?: "",
                    onValueChange = { 
                        val value = it.toDoubleOrNull()
                        onSessionUpdate(session.copy(rearRideHeight = value))
                    },
                    label = { Text("Hauteur caisse arrière") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = session.frontAntiRoll,
                    onValueChange = { onSessionUpdate(session.copy(frontAntiRoll = it)) },
                    label = { Text("Anti-roulis avant") },
                    placeholder = { Text("1.2mm") },
                    modifier = Modifier.weight(1f)
                )
                
                OutlinedTextField(
                    value = session.rearAntiRoll,
                    onValueChange = { onSessionUpdate(session.copy(rearAntiRoll = it)) },
                    label = { Text("Anti-roulis arrière") },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Espacement en bas pour le FAB
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            content()
        }
    }
}