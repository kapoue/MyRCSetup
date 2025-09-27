package com.myrcsetup.app.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * Modèle de données pour une release GitHub
 */
@Serializable
data class GitHubRelease(
    val tag_name: String,
    val name: String,
    val html_url: String,
    val published_at: String,
    val prerelease: Boolean = false,
    val draft: Boolean = false
)

/**
 * Résultat de la vérification de mise à jour
 */
sealed class UpdateCheckResult {
    object NoUpdateAvailable : UpdateCheckResult()
    data class UpdateAvailable(val release: GitHubRelease) : UpdateCheckResult()
    data class Error(val message: String) : UpdateCheckResult()
}

/**
 * Utilitaire pour vérifier les mises à jour via l'API GitHub
 */
class UpdateChecker {
    
    companion object {
        private const val TAG = "UpdateChecker"
        private const val GITHUB_API_URL = "https://api.github.com/repos/kapoue/MyRCSetup/releases/latest"
        private const val GITHUB_RELEASES_URL = "https://github.com/kapoue/MyRCSetup/releases"
        private const val TIMEOUT_MS = 10000 // 10 secondes
        
        private val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
    
    /**
     * Vérifie s'il y a une nouvelle version disponible
     */
    suspend fun checkForUpdate(currentVersion: String): UpdateCheckResult {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Vérification des mises à jour - Version actuelle: $currentVersion")
                
                val url = URL(GITHUB_API_URL)
                val connection = url.openConnection() as HttpURLConnection
                
                connection.apply {
                    requestMethod = "GET"
                    connectTimeout = TIMEOUT_MS
                    readTimeout = TIMEOUT_MS
                    setRequestProperty("Accept", "application/vnd.github.v3+json")
                    setRequestProperty("User-Agent", "MyRCSetup-UpdateChecker")
                }
                
                val responseCode = connection.responseCode
                Log.d(TAG, "Code de réponse GitHub API: $responseCode")
                
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    Log.d(TAG, "Réponse GitHub API reçue")
                    
                    val release = json.decodeFromString<GitHubRelease>(response)
                    Log.d(TAG, "Release parsée: ${release.tag_name}")
                    
                    // Ignorer les versions draft ou prerelease
                    if (release.draft || release.prerelease) {
                        Log.d(TAG, "Release ignorée (draft ou prerelease)")
                        return@withContext UpdateCheckResult.NoUpdateAvailable
                    }
                    
                    // Comparer les versions
                    val isNewerVersion = isNewerVersion(currentVersion, release.tag_name)
                    Log.d(TAG, "Version plus récente disponible: $isNewerVersion")
                    
                    if (isNewerVersion) {
                        UpdateCheckResult.UpdateAvailable(release)
                    } else {
                        UpdateCheckResult.NoUpdateAvailable
                    }
                } else {
                    val errorMessage = "Erreur HTTP: $responseCode"
                    Log.e(TAG, errorMessage)
                    UpdateCheckResult.Error(errorMessage)
                }
            } catch (e: IOException) {
                val errorMessage = "Erreur de connexion: ${e.message}"
                Log.e(TAG, errorMessage, e)
                UpdateCheckResult.Error(errorMessage)
            } catch (e: Exception) {
                val errorMessage = "Erreur inattendue: ${e.message}"
                Log.e(TAG, errorMessage, e)
                UpdateCheckResult.Error(errorMessage)
            }
        }
    }
    
    /**
     * Compare deux versions et détermine si la nouvelle est plus récente
     * Format attendu: "v1.7.11" ou "1.7.11"
     */
    private fun isNewerVersion(currentVersion: String, newVersion: String): Boolean {
        try {
            // Nettoyer les versions (supprimer le 'v' si présent)
            val current = currentVersion.removePrefix("v").trim()
            val new = newVersion.removePrefix("v").trim()
            
            Log.d(TAG, "Comparaison versions: '$current' vs '$new'")
            
            // Diviser en parties (major.minor.patch)
            val currentParts = current.split(".").map { it.toIntOrNull() ?: 0 }
            val newParts = new.split(".").map { it.toIntOrNull() ?: 0 }
            
            // S'assurer qu'on a au moins 3 parties pour chaque version
            val currentNormalized = (currentParts + listOf(0, 0, 0)).take(3)
            val newNormalized = (newParts + listOf(0, 0, 0)).take(3)
            
            Log.d(TAG, "Versions normalisées: $currentNormalized vs $newNormalized")
            
            // Comparer partie par partie
            for (i in 0..2) {
                when {
                    newNormalized[i] > currentNormalized[i] -> {
                        Log.d(TAG, "Nouvelle version détectée à la position $i")
                        return true
                    }
                    newNormalized[i] < currentNormalized[i] -> {
                        Log.d(TAG, "Version actuelle plus récente à la position $i")
                        return false
                    }
                    // Si égal, continuer à la partie suivante
                }
            }
            
            Log.d(TAG, "Versions identiques")
            return false
        } catch (e: Exception) {
            Log.e(TAG, "Erreur lors de la comparaison de versions", e)
            return false
        }
    }
    
    /**
     * Ouvre la page des releases GitHub dans le navigateur
     */
    fun openReleasesPage(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_RELEASES_URL))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            Log.d(TAG, "Page des releases ouverte")
        } catch (e: Exception) {
            Log.e(TAG, "Erreur lors de l'ouverture de la page des releases", e)
        }
    }
}