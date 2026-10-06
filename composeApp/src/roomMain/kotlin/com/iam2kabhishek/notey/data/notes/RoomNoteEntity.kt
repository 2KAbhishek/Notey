package com.iam2kabhishek.notey.data.notes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class RoomNoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long
) {
    fun toNoteEntity() = NoteEntity(id, title, content, createdAt, updatedAt)

    companion object {
        fun from(note: NoteEntity) = RoomNoteEntity(
            id = note.id,
            title = note.title,
            content = note.content,
            createdAt = note.createdAt,
            updatedAt = note.updatedAt
        )
    }
}
