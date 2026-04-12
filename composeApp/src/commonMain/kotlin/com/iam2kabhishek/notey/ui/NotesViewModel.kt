package com.iam2kabhishek.notey.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iam2kabhishek.notey.data.notes.NoteEntity
import com.iam2kabhishek.notey.data.notes.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesViewModel(
    private val noteRepository: NoteRepository
) : ViewModel() {
    val uiState: StateFlow<NotesUiState> = noteRepository
        .getAllNotes()
        .map { notes ->
            NotesUiState(
                notes = notes,
                isLoading = false,
                error = null
            )
        }
        .catch { throwable ->
            emit(
                NotesUiState(
                    notes = emptyList(),
                    isLoading = false,
                    error = throwable.message ?: "Failed to load notes"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = NotesUiState()
        )

    fun createNote(title: String, content: String) {
        viewModelScope.launch {
            noteRepository.createNote(title, content)
        }
    }

    fun updateNote(note: NoteEntity) {
        viewModelScope.launch {
            noteRepository.updateNote(note)
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            noteRepository.deleteNote(note)
        }
    }
}
