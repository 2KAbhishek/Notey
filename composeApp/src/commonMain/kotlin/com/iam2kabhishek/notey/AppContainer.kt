package com.iam2kabhishek.notey

import com.iam2kabhishek.notey.data.createNoteRepository
import com.iam2kabhishek.notey.data.notes.NoteRepository
import com.iam2kabhishek.notey.ui.NotesViewModel

class AppContainer(platformContext: Any) {
    private val noteRepository: NoteRepository = createNoteRepository(platformContext)

    val notesViewModel: NotesViewModel = NotesViewModel(noteRepository)
}
