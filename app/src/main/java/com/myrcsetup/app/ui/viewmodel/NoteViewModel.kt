package com.myrcsetup.app.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.myrcsetup.app.data.entity.Note
import com.myrcsetup.app.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    
    private val _noteContent = MutableStateFlow("")
    val noteContent: StateFlow<String> = _noteContent.asStateFlow()
    
    private val _originalContent = MutableStateFlow("")
    val originalContent: StateFlow<String> = _originalContent.asStateFlow()
    
    private val _hasUnsavedChanges = MutableStateFlow(false)
    val hasUnsavedChanges: StateFlow<Boolean> = _hasUnsavedChanges.asStateFlow()
    
    private val _showSaveDialog = MutableStateFlow(false)
    val showSaveDialog: StateFlow<Boolean> = _showSaveDialog.asStateFlow()
    
    // Flag pour éviter l'apparition automatique du dialogue
    private var isInitialLoad = true
    
    init {
        Log.d("NoteViewModel", "🔧 Initialisation du NoteViewModel")
        loadNote()
    }
    
    private fun loadNote() {
        Log.d("NoteViewModel", "📖 Chargement de la note depuis la base de données")
        viewModelScope.launch {
            repository.getNote().collect { note ->
                val content = note?.content ?: ""
                Log.d("NoteViewModel", "📝 Note chargée: ${content.length} caractères")
                
                _noteContent.value = content
                _originalContent.value = content
                
                // Marquer comme chargement initial terminé
                if (isInitialLoad) {
                    isInitialLoad = false
                    Log.d("NoteViewModel", "✅ Chargement initial terminé")
                }
                
                updateUnsavedChanges()
            }
        }
    }
    
    fun updateNoteContent(content: String) {
        Log.d("NoteViewModel", "✏️ Mise à jour du contenu: ${content.length} caractères")
        _noteContent.value = content
        updateUnsavedChanges()
    }
    
    private fun updateUnsavedChanges() {
        val hasChanges = _noteContent.value != _originalContent.value
        
        // Ne pas déclencher de changements pendant le chargement initial
        if (!isInitialLoad) {
            _hasUnsavedChanges.value = hasChanges
            Log.d("NoteViewModel", "🔄 Modifications non sauvées: $hasChanges")
        }
    }
    
    fun saveNote() {
        Log.d("NoteViewModel", "💾 Sauvegarde de la note")
        viewModelScope.launch {
            repository.saveNote(_noteContent.value)
            _originalContent.value = _noteContent.value
            updateUnsavedChanges()
            Log.d("NoteViewModel", "✅ Note sauvegardée avec succès")
        }
    }
    
    fun showSaveDialog() {
        Log.d("NoteViewModel", "📋 Affichage du dialogue de sauvegarde")
        _showSaveDialog.value = true
    }
    
    fun hideSaveDialog() {
        Log.d("NoteViewModel", "❌ Masquage du dialogue de sauvegarde")
        _showSaveDialog.value = false
    }
    
    fun discardChanges() {
        Log.d("NoteViewModel", "🗑️ Annulation des modifications")
        _noteContent.value = _originalContent.value
        updateUnsavedChanges()
        hideSaveDialog()
    }
    
    fun saveAndExit(onExit: () -> Unit) {
        Log.d("NoteViewModel", "💾➡️ Sauvegarde et sortie")
        viewModelScope.launch {
            saveNote()
            hideSaveDialog()
            onExit()
        }
    }
    
    fun checkUnsavedChangesBeforeExit(onExit: () -> Unit) {
        Log.d("NoteViewModel", "🔍 Vérification des modifications avant sortie")
        if (_hasUnsavedChanges.value) {
            Log.d("NoteViewModel", "⚠️ Modifications détectées, affichage du dialogue")
            showSaveDialog()
        } else {
            Log.d("NoteViewModel", "✅ Aucune modification, sortie directe")
            onExit()
        }
    }
}
