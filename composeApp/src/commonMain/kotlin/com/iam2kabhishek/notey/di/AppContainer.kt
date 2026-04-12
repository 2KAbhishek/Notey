package com.iam2kabhishek.notey.di

import com.iam2kabhishek.notey.data.buildDatabase
import com.iam2kabhishek.notey.data.getDatabaseBuilder
import com.iam2kabhishek.notey.data.repository.NoteRepository
import com.iam2kabhishek.notey.ui.notes.NotesViewModel

class AppContainer(platformContext: Any) {
    private val database = buildDatabase(getDatabaseBuilder(platformContext))
    private val noteRepository = NoteRepository(database.noteDao())

    val notesViewModel: NotesViewModel = NotesViewModel(noteRepository)
}
