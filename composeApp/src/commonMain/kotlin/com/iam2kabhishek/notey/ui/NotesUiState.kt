package com.iam2kabhishek.notey.ui

import com.iam2kabhishek.notey.data.notes.NoteEntity

data class NotesUiState(
    val notes: List<NoteEntity> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
