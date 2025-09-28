package com.myrcsetup.app.ui.viewmodel

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
        loadNote()
    }
    
    private fun loadNote() {
        viewModelScope.launch {
            repository.getNote().collect { note ->
                val content = note?.content ?: ""
                
                _noteContent.value = content
                _originalContent.value = content
                
                // Marquer comme chargement initial terminé
                if (isInitialLoad) {
                    isInitialLoad = false
                }
                
                updateUnsavedChanges()
            }
        }
    }
    
    fun updateNoteContent(content: String) {
        _noteContent.value = content
        updateUnsavedChanges()
    }
    
    private fun updateUnsavedChanges() {
        val hasChanges = _noteContent.value != _originalContent.value
        
        // Ne pas déclencher de changements pendant le chargement initial
        if (!isInitialLoad) {
            _hasUnsavedChanges.value = hasChanges
        }
    }
    
    fun saveNote() {
        viewModelScope.launch {
            repository.saveNote(_noteContent.value)
            _originalContent.value = _noteContent.value
            updateUnsavedChanges()
        }
    }
    
    fun saveNoteAndNavigateHome(onNavigateHome: () -> Unit) {
        viewModelScope.launch {
            repository.saveNote(_noteContent.value)
            _originalContent.value = _noteContent.value
            updateUnsavedChanges()
            onNavigateHome()
        }
    }
    
    fun showSaveDialog() {
        _showSaveDialog.value = true
    }
    
    fun hideSaveDialog() {
        _showSaveDialog.value = false
    }
    
    fun discardChanges() {
        _noteContent.value = _originalContent.value
        updateUnsavedChanges()
        hideSaveDialog()
    }
    
    fun saveAndExit(onExit: () -> Unit) {
        viewModelScope.launch {
            saveNote()
            hideSaveDialog()
            onExit()
        }
    }
    
    fun checkUnsavedChangesBeforeExit(onExit: () -> Unit) {
        if (_hasUnsavedChanges.value) {
            showSaveDialog()
        } else {
            onExit()
        }
    }
}
