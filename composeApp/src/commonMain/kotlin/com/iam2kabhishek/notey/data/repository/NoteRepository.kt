package com.iam2kabhishek.notey.data.repository

import com.iam2kabhishek.notey.data.notes.NoteDao
import com.iam2kabhishek.notey.data.notes.NoteEntity
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock

class NoteRepository(
    private val noteDao: NoteDao
) {
    fun getAllNotes(): Flow<List<NoteEntity>> = noteDao.getAllNotes()

    fun getNoteById(id: Long): Flow<NoteEntity?> = noteDao.getNoteById(id)

    suspend fun createNote(title: String, content: String): Long {
        val now = Clock.System.now().toEpochMilliseconds()
        return noteDao.insertNote(
            NoteEntity(
                title = title,
                content = content,
                createdAt = now,
                updatedAt = now
            )
        )
    }

    suspend fun updateNote(note: NoteEntity) {
        noteDao.updateNote(
            note.copy(updatedAt = Clock.System.now().toEpochMilliseconds())
        )
    }

    suspend fun deleteNote(note: NoteEntity) {
        noteDao.deleteNote(note)
    }
}
