package com.myrcsetup.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.myrcsetup.app.data.database.RCDatabase
import com.myrcsetup.app.data.repository.RCSessionRepository
import com.myrcsetup.app.data.repository.NoteRepository
import com.myrcsetup.app.ui.navigation.RCSetupNavigation
import com.myrcsetup.app.ui.theme.MyRCSetupTheme
import com.myrcsetup.app.ui.viewmodel.RCSessionViewModel
import com.myrcsetup.app.ui.viewmodel.RCSessionViewModelFactory
import com.myrcsetup.app.ui.viewmodel.NoteViewModel
import com.myrcsetup.app.ui.viewmodel.NoteViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Log de démarrage pour identifier la version
        android.util.Log.d("MyRCSetup", "=== APPLICATION STARTED - VERSION 1.9.0 (Build 38) ===")
        
        val database = RCDatabase.getDatabase(this)
        val sessionRepository = RCSessionRepository(database.sessionDao())
        val noteRepository = NoteRepository(database.noteDao())
        val sessionViewModelFactory = RCSessionViewModelFactory(sessionRepository)
        val noteViewModelFactory = NoteViewModelFactory(noteRepository)
        
        setContent {
            MyRCSetupTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val sessionViewModel: RCSessionViewModel = viewModel(factory = sessionViewModelFactory)
                        val noteViewModel: NoteViewModel = viewModel(factory = noteViewModelFactory)
                        RCSetupNavigation(
                            sessionViewModel = sessionViewModel,
                            noteViewModel = noteViewModel
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyRCSetupTheme {
        // Preview content
    }
}