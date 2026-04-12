package com.iam2kabhishek.notey.data

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual fun getDatabaseBuilder(context: Any): RoomDatabase.Builder<AppDatabase> {
    val dbDir = File(System.getProperty("user.home"), ".notey")
    if (!dbDir.exists()) {
        dbDir.mkdirs()
    }
    val dbFile = File(dbDir, "notey.db")
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath
    )
}