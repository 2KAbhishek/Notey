package com.iam2kabhishek.notey.data

import com.iam2kabhishek.notey.data.notes.NoteDao
import com.iam2kabhishek.notey.data.notes.NoteEntity
import com.iam2kabhishek.notey.data.notes.NoteRepository
import kotlinx.browser.window
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

private const val LOCAL_STORAGE_KEY = "notey_notes_v1"

private fun escapeJson(s: String): String = s
    .replace("\\", "\\\\")
    .replace("\"", "\\\"")
    .replace("\n", "\\n")
    .replace("\r", "\\r")
    .replace("\t", "\\t")

private fun unescapeJson(s: String): String = s
    .replace("\\n", "\n")
    .replace("\\r", "\r")
    .replace("\\t", "\t")
    .replace("\\\"", "\"")
    .replace("\\\\", "\\")

private fun NoteEntity.toJson(): String =
    """{"id":$id,"title":"${escapeJson(title)}","content":"${escapeJson(content)}","createdAt":$createdAt,"updatedAt":$updatedAt}"""

private fun List<NoteEntity>.toJson(): String =
    "[" + joinToString(",") { it.toJson() } + "]"

private fun parseNotes(raw: String?): List<NoteEntity> {
    if (raw.isNullOrBlank()) return emptyList()
    val pattern = Regex("""\{"id":(-?\d+),"title":"((?:\\.|[^"\\])*)","content":"((?:\\.|[^"\\])*)","createdAt":(-?\d+),"updatedAt":(-?\d+)\}""")
    return pattern.findAll(raw).mapNotNull { match ->
        try {
            val (idStr, titleStr, contentStr, createdStr, updatedStr) = match.destructured
            NoteEntity(
                id = idStr.toLong(),
                title = unescapeJson(titleStr),
                content = unescapeJson(contentStr),
                createdAt = createdStr.toLong(),
                updatedAt = updatedStr.toLong()
            )
        } catch (_: Exception) {
            null
        }
    }.toList()
}

private class LocalStorageNoteDao : NoteDao {
    private val _notes = MutableStateFlow(loadNotes())

    private fun loadNotes(): List<NoteEntity> {
        return try {
            val stored = window.localStorage.getItem(LOCAL_STORAGE_KEY)
            parseNotes(stored)
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun persist(notes: List<NoteEntity>) {
        _notes.value = notes
        try {
            window.localStorage.setItem(LOCAL_STORAGE_KEY, notes.toJson())
        } catch (_: Exception) {
            // storage quota exceeded or disabled in private browsing
        }
    }

    override fun getAllNotes(): Flow<List<NoteEntity>> = _notes

    override fun getNoteById(id: Long): Flow<NoteEntity?> =
        _notes.map { list -> list.firstOrNull { it.id == id } }

    override suspend fun insertNote(note: NoteEntity): Long {
        val newId = if (note.id == 0L) (_notes.value.maxOfOrNull { it.id } ?: 0L) + 1 else note.id
        val newNote = note.copy(id = newId)
        val updated = (_notes.value.filter { it.id != newId } + newNote).sortedByDescending { it.updatedAt }
        persist(updated)
        return newId
    }

    override suspend fun updateNote(note: NoteEntity) {
        val updated = _notes.value.map { if (it.id == note.id) note else it }.sortedByDescending { it.updatedAt }
        persist(updated)
    }

    override suspend fun deleteNote(note: NoteEntity) {
        val updated = _notes.value.filter { it.id != note.id }
        persist(updated)
    }
}

actual fun createNoteRepository(platformContext: Any): NoteRepository {
    return NoteRepository(LocalStorageNoteDao())
}
