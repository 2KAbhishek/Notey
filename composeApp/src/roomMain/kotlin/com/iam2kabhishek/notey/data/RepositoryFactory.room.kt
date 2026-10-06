package com.iam2kabhishek.notey.data

import com.iam2kabhishek.notey.data.notes.NoteRepository

actual fun createNoteRepository(platformContext: Any): NoteRepository {
    val database = buildDatabase(getDatabaseBuilder(platformContext))
    return NoteRepository(database.noteDao())
}
