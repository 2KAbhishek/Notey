package com.iam2kabhishek.notey

import com.iam2kabhishek.notey.data.buildDatabase
import com.iam2kabhishek.notey.data.getDatabaseBuilder
import com.iam2kabhishek.notey.data.notes.NoteRepository
import com.iam2kabhishek.notey.ui.NotesViewModel

class AppContainer(platformContext: Any) {
    private val database = buildDatabase(getDatabaseBuilder(platformContext))
    private val noteRepository = NoteRepository(database.noteDao())

    val notesViewModel: NotesViewModel = NotesViewModel(noteRepository)
}