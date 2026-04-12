package com.iam2kabhishek.notey.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSFileManager

actual fun getDatabaseBuilder(context: Any): RoomDatabase.Builder<AppDatabase> {
    val documentDir = NSFileManager.defaultManager.URLForDirectory(
        directory = NSFileManager.NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
    val dbPath = "${requireNotNull(documentDir?.path)}/notey.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbPath
    )
}