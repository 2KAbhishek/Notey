package com.iam2kabhishek.notey.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabaseConstructor
import androidx.room.RoomDatabase

@Database(
    entities = [NoteEntity::class],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
