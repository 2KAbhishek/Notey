package com.iam2kabhishek.notey.data.notes

import kotlinx.coroutines.flow.Flow

interface NoteDao {
    fun getAllNotes(): Flow<List<NoteEntity>>
    fun getNoteById(id: Long): Flow<NoteEntity?>
    suspend fun insertNote(note: NoteEntity): Long
    suspend fun updateNote(note: NoteEntity)
    suspend fun deleteNote(note: NoteEntity)
}
