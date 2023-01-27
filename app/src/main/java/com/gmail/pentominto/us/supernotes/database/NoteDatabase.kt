package com.gmail.pentominto.us.supernotes.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gmail.pentominto.us.supernotes.data.entities.Category
import com.gmail.pentominto.us.supernotes.data.entities.Note
import com.gmail.pentominto.us.supernotes.data.entities.TrashNote

@Database(
    entities = [Note::class, Category::class, TrashNote::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNoteDatabaseDao() : DatabaseDao
}