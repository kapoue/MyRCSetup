package com.myrcsetup.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myrcsetup.app.data.entity.RCSession
import com.myrcsetup.app.ui.viewmodel.RCSessionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionListScreen(
    viewModel: RCSessionViewModel,
    onNavigateToNewSession: () -> Unit,
    onNavigateToEditSession: (Long) -> Unit
) {
    val sessions by viewModel.sessions.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf<RCSession?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My RC Setup") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToNewSession
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nouvelle Session")
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sessions, key = { it.id }) { session ->
                    SessionCard(
                        session = session,
                        onEdit = { onNavigateToEditSession(session.id) },
                        onDelete = { showDeleteDialog = session }
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
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onEdit
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = session.carName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = session.trackName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date()),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Modifier"
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Supprimer",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            
            if (session.bestLapTime.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Meilleur temps: ${session.bestLapTime}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            if (session.comments.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
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