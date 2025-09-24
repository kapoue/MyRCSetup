package com.myrcsetup.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myrcsetup.app.ui.screens.AboutScreen
import com.myrcsetup.app.ui.screens.SessionFormScreen
import com.myrcsetup.app.ui.screens.SessionListScreen
import com.myrcsetup.app.ui.viewmodel.RCSessionViewModel

@Composable
fun RCSetupNavigation(
    navController: NavHostController = rememberNavController(),
    viewModel: RCSessionViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "session_list"
    ) {
        composable("session_list") {
            SessionListScreen(
                viewModel = viewModel,
                onNavigateToNewSession = {
                    viewModel.createNewSession()
                    navController.navigate("session_form")
                },
                onNavigateToEditSession = { sessionId ->
                    viewModel.loadSessionForEdit(sessionId)
                    navController.navigate("session_form")
                },
                onNavigateToAbout = {
                    navController.navigate("about")
                }
            )
        }
        
        composable("session_form") {
            SessionFormScreen(
                viewModel = viewModel,
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
    }
}