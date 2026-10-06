package com.iam2kabhishek.notey.data.notes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Dao
interface RoomNoteDao : NoteDao {
    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun getAllRoomNotes(): Flow<List<RoomNoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getRoomNoteById(id: Long): Flow<RoomNoteEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoomNote(note: RoomNoteEntity): Long

    @Update
    suspend fun updateRoomNote(note: RoomNoteEntity)

    @Delete
    suspend fun deleteRoomNote(note: RoomNoteEntity)

    override fun getAllNotes(): Flow<List<NoteEntity>> =
        getAllRoomNotes().map { list -> list.map { it.toNoteEntity() } }

    override fun getNoteById(id: Long): Flow<NoteEntity?> =
        getRoomNoteById(id).map { it?.toNoteEntity() }

    override suspend fun insertNote(note: NoteEntity): Long =
        insertRoomNote(RoomNoteEntity.from(note))

    override suspend fun updateNote(note: NoteEntity) =
        updateRoomNote(RoomNoteEntity.from(note))

    override suspend fun deleteNote(note: NoteEntity) =
        deleteRoomNote(RoomNoteEntity.from(note))
}
