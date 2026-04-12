package com.iam2kabhishek.notey

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.iam2kabhishek.notey.data.notes.NoteEntity
import com.iam2kabhishek.notey.di.AppContainer
import com.iam2kabhishek.notey.ui.screens.NoteDetailScreen
import com.iam2kabhishek.notey.ui.screens.NoteListScreen

sealed class NoteScreen {
    data object List : NoteScreen()
    data class Detail(val note: NoteEntity?) : NoteScreen()
}

@Composable
fun App(platformContext: Any) {
    val appContainer = remember(platformContext) { AppContainer(platformContext) }
    val uiState by appContainer.notesViewModel.uiState.collectAsState()
    
    var currentScreen by remember { mutableStateOf<NoteScreen>(NoteScreen.List) }
    
    when (val screen = currentScreen) {
        is NoteScreen.List -> {
            NoteListScreen(
                uiState = uiState,
                onNoteClick = { note -> 
                    currentScreen = NoteScreen.Detail(note)
                },
                onAddClick = { 
                    currentScreen = NoteScreen.Detail(null)
                }
            )
        }
        is NoteScreen.Detail -> {
            NoteDetailScreen(
                existingNote = screen.note,
                onSave = { title, content ->
                    if (screen.note != null) {
                        appContainer.notesViewModel.updateNote(
                            screen.note.copy(title = title, content = content)
                        )
                    } else {
                        appContainer.notesViewModel.createNote(title, content)
                    }
                    currentScreen = NoteScreen.List
                },
                onDelete = {
                    screen.note?.let { note ->
                        appContainer.notesViewModel.deleteNote(note)
                    }
                    currentScreen = NoteScreen.List
                },
                onBack = { 
                    currentScreen = NoteScreen.List
                }
            )
        }
    }
}