package com.gmail.pentominto.us.supernotes.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.data.Note

@Database(entities = [Note::class, Category::class] , version = 1 , exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNoteDatabaseDao() : DatabaseDao
}