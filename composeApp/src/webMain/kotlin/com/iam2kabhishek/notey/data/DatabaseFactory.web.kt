package com.iam2kabhishek.notey.data

import com.iam2kabhishek.notey.data.notes.NoteDao
import com.iam2kabhishek.notey.data.notes.NoteEntity
import com.iam2kabhishek.notey.data.notes.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

private class InMemoryNoteDao : NoteDao {
    private val _notes = MutableStateFlow<List<NoteEntity>>(emptyList())

    override fun getAllNotes(): Flow<List<NoteEntity>> = _notes

    override fun getNoteById(id: Long): Flow<NoteEntity?> =
        _notes.map { list -> list.firstOrNull { it.id == id } }

    override suspend fun insertNote(note: NoteEntity): Long {
        val newId = if (note.id == 0L) (_notes.value.maxOfOrNull { it.id } ?: 0L) + 1 else note.id
        val newNote = note.copy(id = newId)
        _notes.value = (_notes.value.filter { it.id != newId } + newNote).sortedByDescending { it.updatedAt }
        return newId
    }

    override suspend fun updateNote(note: NoteEntity) {
        _notes.value = _notes.value.map { if (it.id == note.id) note else it }.sortedByDescending { it.updatedAt }
    }

    override suspend fun deleteNote(note: NoteEntity) {
        _notes.value = _notes.value.filter { it.id != note.id }
    }
}

actual fun createNoteRepository(platformContext: Any): NoteRepository {
    return NoteRepository(InMemoryNoteDao())
}
