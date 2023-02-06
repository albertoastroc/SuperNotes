package com.gmail.pentominto.us.supernotes.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gmail.pentominto.us.supernotes.data.NoteCategory
import com.gmail.pentominto.us.supernotes.data.SavedNote
import com.gmail.pentominto.us.supernotes.data.DiscardedNote

@Database(
    entities = [SavedNote::class, NoteCategory::class, DiscardedNote::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNoteDatabaseDao() : DatabaseDao
}