package com.iam2kabhishek.notey.data.notes

data class NoteEntity(
    val id: Long = 0,
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long
)
