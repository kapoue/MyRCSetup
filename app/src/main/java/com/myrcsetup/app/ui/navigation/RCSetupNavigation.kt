package com.myrcsetup.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myrcsetup.app.ui.screens.AboutScreen
import com.myrcsetup.app.ui.screens.NotesScreen
import com.myrcsetup.app.ui.screens.SessionFormScreen
import com.myrcsetup.app.ui.screens.SessionListScreen
import com.myrcsetup.app.ui.viewmodel.NoteViewModel
import com.myrcsetup.app.ui.viewmodel.RCSessionViewModel

@Composable
fun RCSetupNavigation(
    navController: NavHostController = rememberNavController(),
    sessionViewModel: RCSessionViewModel,
    noteViewModel: NoteViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "session_list"
    ) {
        composable("session_list") {
            SessionListScreen(
                viewModel = sessionViewModel,
                onNavigateToNewSession = {
                    sessionViewModel.createNewSession()
                    navController.navigate("session_form")
                },
                onNavigateToEditSession = { sessionId ->
                    sessionViewModel.loadSessionForEdit(sessionId)
                    navController.navigate("session_form")
                },
                onNavigateToAbout = {
                    navController.navigate("about")
                },
                onNavigateToNotes = {
                    navController.navigate("notes")
                }
            )
        }
        
        composable("session_form") {
            SessionFormScreen(
                viewModel = sessionViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("about") {
            AboutScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("notes") {
            NotesScreen(
                viewModel = noteViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}