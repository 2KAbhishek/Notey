package com.iam2kabhishek.notey.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iam2kabhishek.notey.data.local.NoteDao
import com.iam2kabhishek.notey.data.local.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}