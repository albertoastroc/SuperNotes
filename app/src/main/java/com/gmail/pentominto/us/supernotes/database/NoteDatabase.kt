package com.gmail.pentominto.us.supernotes.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.data.Note
import com.gmail.pentominto.us.supernotes.data.TrashNote

@Database(entities = [Note::class, Category::class, TrashNote::class] , version = 2 , exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNoteDatabaseDao() : DatabaseDao
}