package com.gmail.pentominto.us.supernotes.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gmail.pentominto.us.supernotes.data.entities.CategoryEntity
import com.gmail.pentominto.us.supernotes.data.entities.NoteEntity
import com.gmail.pentominto.us.supernotes.data.entities.TrashNoteEntity

@Database(
    entities = [NoteEntity::class, CategoryEntity::class, TrashNoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNoteDatabaseDao() : DatabaseDao
}