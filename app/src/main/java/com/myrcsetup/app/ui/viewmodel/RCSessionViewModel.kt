package com.myrcsetup.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.myrcsetup.app.data.entity.RCSession
import com.myrcsetup.app.data.repository.RCSessionRepository
import com.myrcsetup.app.data.model.ExportData
import com.myrcsetup.app.data.model.toSerializable
import com.myrcsetup.app.data.model.toRCSession
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RCSessionViewModel(private val repository: RCSessionRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(RCSessionUiState())
    val uiState: StateFlow<RCSessionUiState> = _uiState.asStateFlow()
    
    val sessions = repository.getAllSessions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    fun loadSessionForEdit(sessionId: Long) {
        viewModelScope.launch {
            val session = repository.getSessionById(sessionId)
            session?.let {
                _uiState.value = _uiState.value.copy(
                    currentSession = it,
                    isEditing = true
                )
            }
        }
    }
    
    fun createNewSession() {
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val newSession = RCSession(
            carName = "",
            trackName = "",
            dateTime = currentDateTime,
            bestLapTime = "",
            comments = "",
            frontSprings = "",
            rearSprings = "",
            frontShockOil = null,
            rearShockOil = null,
            shockPosition = "",
            frontDiffOil = null,
            rearDiffOil = null,
            centerDiffOil = null,
            frontCamber = null,
            rearCamber = null,
            frontToe = null,
            rearToe = null,
            caster = null,
            pinion = null,
            spurGear = null,
            frontTires = "",
            rearTires = "",
            tireFoam = "",
            tractionAdditive = "",
            chassisStiffness = "",
            frontRideHeight = null,
            rearRideHeight = null,
            frontAntiRoll = null,
            rearAntiRoll = null
        )
        
        _uiState.value = _uiState.value.copy(
            currentSession = newSession,
            isEditing = false
        )
    }
    
    fun updateCurrentSession(session: RCSession) {
        _uiState.value = _uiState.value.copy(currentSession = session)
    }
    
    fun saveSession() {
        viewModelScope.launch {
            val session = _uiState.value.currentSession
            if (session != null) {
                try {
                    if (_uiState.value.isEditing) {
                        repository.updateSession(session)
                    } else {
                        repository.insertSession(session)
                    }
                    _uiState.value = _uiState.value.copy(
                        currentSession = null,
                        isEditing = false,
                        saveSuccess = true
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Erreur lors de la sauvegarde: ${e.message}"
                    )
                }
            }
        }
    }
    
    fun deleteSession(session: RCSession) {
        viewModelScope.launch {
            try {
                repository.deleteSession(session)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Erreur lors de la suppression: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    fun clearSaveSuccess() {
        _uiState.value = _uiState.value.copy(saveSuccess = false)
    }
    
    fun searchSessions(query: String): Flow<List<RCSession>> {
        return if (query.isBlank()) {
            repository.getAllSessions()
        } else {
            repository.searchSessions(query)
        }
    }
    
    fun exportDataToJson(context: Context) {
        viewModelScope.launch {
            try {
                val allSessions = repository.getAllSessions().first()
                val currentDateTime = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHhMMmSSs")
                val timestamp = currentDateTime.format(formatter)
                
                val exportData = ExportData(
                    version = "1.0",
                    appVersion = "1.6.1",
                    exportDate = currentDateTime.toString(),
                    sessions = allSessions.map { it.toSerializable() }
                )
                
                val jsonString = Json.encodeToString(exportData)
                val fileName = "MyRCSetup_Export_$timestamp.json"
                
                // Créer un fichier temporaire
                val cacheDir = File(context.cacheDir, "exports")
                if (!cacheDir.exists()) cacheDir.mkdirs()
                
                val file = File(cacheDir, fileName)
                file.writeText(jsonString)
                
                // Créer l'Intent de partage
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
                
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "application/json"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    putExtra(Intent.EXTRA_SUBJECT, "Export My RC Setup - $timestamp")
                    putExtra(Intent.EXTRA_TEXT, "Sauvegarde de mes réglages RC du $timestamp")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                
                _uiState.value = _uiState.value.copy(
                    shareIntent = Intent.createChooser(shareIntent, "Exporter les données"),
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Erreur lors de l'export: ${e.message}"
                )
            }
        }
    }
    
    fun importDataFromJson(context: Context) {
        // Créer l'Intent pour sélectionner un fichier
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/json", "text/plain"))
        }
        
        _uiState.value = _uiState.value.copy(
            shareIntent = Intent.createChooser(intent, "Sélectionner un fichier de sauvegarde")
        )
    }
    
    fun importDataFromUri(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val jsonString = inputStream?.bufferedReader()?.use { it.readText() }
                
                if (jsonString != null) {
                    importDataFromJsonString(jsonString)
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Impossible de lire le fichier sélectionné"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Erreur lors de la lecture du fichier: ${e.message}"
                )
            }
        }
    }
    
    fun importDataFromJsonString(jsonString: String) {
        viewModelScope.launch {
            try {
                val importData = Json.decodeFromString<ExportData>(jsonString)
                
                // Validation de la version
                if (importData.version != "1.0") {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Version de données non supportée: ${importData.version}"
                    )
                    return@launch
                }
                
                // Import des sessions
                val sessionsToImport = importData.sessions.map { it.toRCSession() }
                
                for (session in sessionsToImport) {
                    repository.insertSession(session)
                }
                
                _uiState.value = _uiState.value.copy(
                    importSuccess = true,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Erreur lors de l'import: ${e.message}"
                )
            }
        }
    }
    
    fun clearExportSuccess() {
        _uiState.value = _uiState.value.copy(
            exportSuccess = false,
            exportData = null,
            shareIntent = null
        )
    }
    
    fun clearShareIntent() {
        _uiState.value = _uiState.value.copy(shareIntent = null)
    }
    
    fun clearImportSuccess() {
        _uiState.value = _uiState.value.copy(importSuccess = false)
    }
    
    fun getExportFileName(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHhMMmSSs")
        val timestamp = currentDateTime.format(formatter)
        return "MyRCSetup_Export_$timestamp.json"
    }
}

data class RCSessionUiState(
    val currentSession: RCSession? = null,
    val isEditing: Boolean = false,
    val errorMessage: String? = null,
    val saveSuccess: Boolean = false,
    val exportSuccess: Boolean = false,
    val importSuccess: Boolean = false,
    val exportData: String? = null,
    val shareIntent: Intent? = null
)

class RCSessionViewModelFactory(private val repository: RCSessionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RCSessionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RCSessionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}