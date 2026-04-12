package com.iam2kabhishek.notey.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual fun getDatabaseBuilder(context: Any): RoomDatabase.Builder<AppDatabase> {
    val androidContext = context as Context
    val dbFile = androidContext.getDatabasePath("notey.db")
    return Room.databaseBuilder<AppDatabase>(
        context = androidContext,
        name = dbFile.absolutePath
    )
}