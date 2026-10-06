package com.iam2kabhishek.notey.data

import com.iam2kabhishek.notey.data.notes.NoteRepository

expect fun createNoteRepository(platformContext: Any): NoteRepository
