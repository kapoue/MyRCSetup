package com.myrcsetup.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.graphics.Bitmap
import androidx.core.content.FileProvider
import com.myrcsetup.app.data.entity.RCSession
import com.myrcsetup.app.data.repository.RCSessionRepository
import com.myrcsetup.app.data.model.ExportData
import com.myrcsetup.app.data.model.toSerializable
import com.myrcsetup.app.data.model.toRCSession
import com.myrcsetup.app.utils.QRCodeGenerator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import java.io.File
import java.io.FileOutputStream
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
                    originalSession = it.copy(),
                    isEditing = true,
                    hasUnsavedChanges = false
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
            originalSession = newSession.copy(),
            isEditing = false,
            hasUnsavedChanges = false
        )
    }
    
    fun updateCurrentSession(session: RCSession) {
        val originalSession = _uiState.value.originalSession
        val hasChanges = originalSession != null && originalSession != session
        
        _uiState.value = _uiState.value.copy(
            currentSession = session,
            hasUnsavedChanges = hasChanges
        )
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
                
                // Vérifier s'il y a des sessions à exporter
                if (allSessions.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Aucune session à exporter. Créez d'abord une session."
                    )
                    return@launch
                }
                
                val currentDateTime = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHhMMmSSs")
                val timestamp = currentDateTime.format(formatter)
                
                val exportData = ExportData(
                    version = "1.0",
                    appVersion = "1.7.3",
                    exportDate = currentDateTime.toString(),
                    sessions = allSessions.map { it.toSerializable() }
                )
                
                val jsonString = Json.encodeToString(exportData)
                val fileName = "MyRCSetup_Export_$timestamp.json"
                
                // Créer un fichier temporaire
                val cacheDir = File(context.cacheDir, "exports")
                if (!cacheDir.exists()) {
                    val created = cacheDir.mkdirs()
                    if (!created) {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "Impossible de créer le dossier d'export"
                        )
                        return@launch
                    }
                }
                
                val file = File(cacheDir, fileName)
                file.writeText(jsonString)
                
                // Vérifier que le fichier a été créé
                if (!file.exists()) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Impossible de créer le fichier d'export"
                    )
                    return@launch
                }
                
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
                    putExtra(Intent.EXTRA_TEXT, "Sauvegarde de mes réglages RC du $timestamp (${allSessions.size} sessions)")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                
                _uiState.value = _uiState.value.copy(
                    shareIntent = Intent.createChooser(shareIntent, "Exporter les données"),
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Erreur lors de l'export: ${e.message} - ${e.javaClass.simpleName}"
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
    
    /**
     * Partage une session individuelle via QR Code
     */
    fun shareSessionViaQR(session: RCSession) {
        viewModelScope.launch {
            try {
                // Convertir la session en JSON
                val sessionData = ExportData(
                    version = "1.0",
                    appVersion = "1.7.0",
                    exportDate = LocalDateTime.now().toString(),
                    sessions = listOf(session.toSerializable())
                )
                
                val jsonString = Json.encodeToString(sessionData)
                
                // Vérifier si les données peuvent être encodées en QR
                if (!QRCodeGenerator.canEncodeData(jsonString)) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Les données de cette session sont trop volumineuses pour un QR code. Utilisez l'export JSON classique."
                    )
                    return@launch
                }
                
                // Générer le QR code
                val qrBitmap = QRCodeGenerator.generateQRCode(
                    data = jsonString,
                    width = QRCodeGenerator.getOptimalSize(jsonString),
                    height = QRCodeGenerator.getOptimalSize(jsonString)
                )
                
                if (qrBitmap != null) {
                    _uiState.value = _uiState.value.copy(
                        qrCodeBitmap = qrBitmap,
                        qrCodeData = jsonString,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Impossible de générer le QR code pour cette session"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Erreur lors de la génération du QR code: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Partage le QR code généré via Intent
     */
    fun shareQRCode(context: Context) {
        viewModelScope.launch {
            try {
                val qrBitmap = _uiState.value.qrCodeBitmap
                val session = _uiState.value.currentSession
                
                if (qrBitmap == null || session == null) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Aucun QR code à partager"
                    )
                    return@launch
                }
                
                // Créer un fichier temporaire pour l'image
                val cacheDir = File(context.cacheDir, "qr_codes")
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs()
                }
                
                val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HHhMMmSSs"))
                val fileName = "QR_${session.carName.replace(" ", "_")}_$timestamp.png"
                val file = File(cacheDir, fileName)
                
                // Sauvegarder le bitmap
                FileOutputStream(file).use { out ->
                    qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }
                
                // Créer l'Intent de partage
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
                
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "image/png"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    putExtra(Intent.EXTRA_SUBJECT, "Réglage RC - ${session.carName}")
                    putExtra(Intent.EXTRA_TEXT, "Réglage pour ${session.carName} sur ${session.trackName}")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                
                _uiState.value = _uiState.value.copy(
                    shareIntent = Intent.createChooser(shareIntent, "Partager le QR Code"),
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Erreur lors du partage du QR code: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Importe une session depuis un QR code scanné
     */
    fun importSessionFromQR(qrContent: String) {
        viewModelScope.launch {
            try {
                // Valider le contenu du QR code
                if (!com.myrcsetup.app.utils.isValidRCSessionQR(qrContent)) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Le QR code scanné ne contient pas de données de session RC valides"
                    )
                    return@launch
                }
                
                // Décoder les données
                val importData = Json.decodeFromString<ExportData>(qrContent)
                
                // Validation de la version
                if (importData.version != "1.0") {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Version de données non supportée: ${importData.version}"
                    )
                    return@launch
                }
                
                // Import de la session (normalement une seule pour un QR code)
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
                    errorMessage = "Erreur lors de l'import du QR code: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Démarre le processus de scan QR code
     */
    fun startQRCodeScanning() {
        _uiState.value = _uiState.value.copy(
            isQRScanningActive = true,
            errorMessage = null
        )
    }
    
    /**
     * Arrête le processus de scan QR code
     */
    fun stopQRCodeScanning() {
        _uiState.value = _uiState.value.copy(
            isQRScanningActive = false
        )
    }
    
    /**
     * Demande de navigation retour avec vérification des modifications
     */
    fun requestNavigateBack() {
        if (_uiState.value.hasUnsavedChanges) {
            _uiState.value = _uiState.value.copy(showUnsavedChangesDialog = true)
        } else {
            // Navigation directe si pas de modifications
            _uiState.value = _uiState.value.copy(
                currentSession = null,
                originalSession = null,
                isEditing = false,
                hasUnsavedChanges = false
            )
        }
    }
    
    /**
     * Sauvegarde et quitte
     */
    fun saveAndExit() {
        _uiState.value = _uiState.value.copy(showUnsavedChangesDialog = false)
        saveSession()
    }
    
    /**
     * Quitte sans sauvegarder
     */
    fun exitWithoutSaving() {
        _uiState.value = _uiState.value.copy(
            currentSession = null,
            originalSession = null,
            isEditing = false,
            hasUnsavedChanges = false,
            showUnsavedChangesDialog = false,
            saveSuccess = true // Pour déclencher la navigation
        )
    }
    
    /**
     * Annule la sortie
     */
    fun cancelExit() {
        _uiState.value = _uiState.value.copy(showUnsavedChangesDialog = false)
    }
    
    /**
     * Efface le QR code généré
     */
    fun clearQRCode() {
        _uiState.value = _uiState.value.copy(
            qrCodeBitmap = null,
            qrCodeData = null
        )
    }
}

data class RCSessionUiState(
    val currentSession: RCSession? = null,
    val originalSession: RCSession? = null,
    val isEditing: Boolean = false,
    val errorMessage: String? = null,
    val saveSuccess: Boolean = false,
    val exportSuccess: Boolean = false,
    val importSuccess: Boolean = false,
    val exportData: String? = null,
    val shareIntent: Intent? = null,
    val qrCodeBitmap: Bitmap? = null,
    val qrCodeData: String? = null,
    val isQRScanningActive: Boolean = false,
    val hasUnsavedChanges: Boolean = false,
    val showUnsavedChangesDialog: Boolean = false
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