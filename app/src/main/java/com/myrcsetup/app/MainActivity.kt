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
        android.util.Log.d("MyRCSetup", "=== APPLICATION STARTED - VERSION 1.9.2 (Build 40) ===")
        
        try {
            android.util.Log.d("MyRCSetup", "🔄 Initialisation de la base de données...")
            val database = RCDatabase.getDatabase(this)
            android.util.Log.d("MyRCSetup", "✅ Base de données initialisée")
            
            android.util.Log.d("MyRCSetup", "🔄 Création des repositories...")
            val sessionRepository = RCSessionRepository(database.sessionDao())
            val noteRepository = NoteRepository(database.noteDao())
            android.util.Log.d("MyRCSetup", "✅ Repositories créés")
            
            android.util.Log.d("MyRCSetup", "🔄 Création des ViewModelFactory...")
            val sessionViewModelFactory = RCSessionViewModelFactory(sessionRepository)
            val noteViewModelFactory = NoteViewModelFactory(noteRepository)
            android.util.Log.d("MyRCSetup", "✅ ViewModelFactory créés")
        
            android.util.Log.d("MyRCSetup", "🔄 Initialisation de l'interface utilisateur...")
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
                            android.util.Log.d("MyRCSetup", "🔄 Création des ViewModels...")
                            val sessionViewModel: RCSessionViewModel = viewModel(factory = sessionViewModelFactory)
                            val noteViewModel: NoteViewModel = viewModel(factory = noteViewModelFactory)
                            android.util.Log.d("MyRCSetup", "✅ ViewModels créés")
                            
                            android.util.Log.d("MyRCSetup", "🔄 Initialisation de la navigation...")
                            RCSetupNavigation(
                                sessionViewModel = sessionViewModel,
                                noteViewModel = noteViewModel
                            )
                            android.util.Log.d("MyRCSetup", "✅ Navigation initialisée")
                        }
                    }
                }
            }
            android.util.Log.d("MyRCSetup", "🎉 APPLICATION DÉMARRÉE AVEC SUCCÈS!")
        } catch (e: Exception) {
            android.util.Log.e("MyRCSetup", "💥 ERREUR CRITIQUE lors du démarrage: ${e.message}", e)
            throw e
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