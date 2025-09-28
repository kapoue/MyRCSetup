package com.myrcsetup.app.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.graphics.Color
import com.myrcsetup.app.ui.viewmodel.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    viewModel: NoteViewModel,
    onNavigateBack: () -> Unit
) {
    val noteContent by viewModel.noteContent.collectAsStateWithLifecycle()
    val hasUnsavedChanges by viewModel.hasUnsavedChanges.collectAsStateWithLifecycle()
    val showSaveDialog by viewModel.showSaveDialog.collectAsStateWithLifecycle()
    
    // Gestion du retour arrière avec dialogue de confirmation
    BackHandler {
        viewModel.checkUnsavedChangesBeforeExit(onNavigateBack)
    }
    
    // Dialogue de confirmation pour les modifications non sauvées
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideSaveDialog() },
            title = { Text("Modifications non sauvegardées") },
            text = { Text("Voulez-vous sauvegarder vos modifications avant de quitter ?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.saveAndExit(onNavigateBack)
                    }
                ) {
                    Text("Sauvegarder")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.discardChanges()
                        onNavigateBack()
                    }
                ) {
                    Text("Ignorer")
                }
            }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bloc-notes") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.checkUnsavedChangesBeforeExit(onNavigateBack)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Retour"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.saveNote() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Sauvegarder",
                            tint = Color.White // Toujours blanc, même avec modifications
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF6600), // Orange Racing
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            BasicTextField(
                value = noteContent,
                onValueChange = { viewModel.updateNoteContent(it) },
                modifier = Modifier.fillMaxSize(),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                cursorBrush = SolidColor(Color(0xFF00BFFF)), // Electric Blue
                decorationBox = { innerTextField ->
                    if (noteContent.isEmpty()) {
                        Text(
                            text = "Tapez vos notes ici...",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}