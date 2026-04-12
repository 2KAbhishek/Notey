package com.iam2kabhishek.notey.ui.notes

import com.iam2kabhishek.notey.data.local.NoteEntity

data class NotesUiState(
    val notes: List<NoteEntity> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
