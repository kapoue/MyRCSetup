package com.myrcsetup.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.myrcsetup.app.data.entity.RCSession
import com.myrcsetup.app.data.repository.RCSessionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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
            frontToe = "",
            rearToe = "",
            caster = null,
            pinion = null,
            spurGear = null,
            finalRatio = null,
            frontTires = "",
            rearTires = "",
            tireFoam = "",
            tractionAdditive = "",
            chassisStiffness = "",
            frontRideHeight = null,
            rearRideHeight = null,
            frontAntiRoll = "",
            rearAntiRoll = ""
        )
        
        _uiState.value = _uiState.value.copy(
            currentSession = newSession,
            isEditing = false
        )
    }
    
    fun updateCurrentSession(session: RCSession) {
        val updatedSession = session.copy(
            finalRatio = session.calculateFinalRatio()
        )
        _uiState.value = _uiState.value.copy(currentSession = updatedSession)
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
}

data class RCSessionUiState(
    val currentSession: RCSession? = null,
    val isEditing: Boolean = false,
    val errorMessage: String? = null,
    val saveSuccess: Boolean = false
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