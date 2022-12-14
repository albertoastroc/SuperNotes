package com.gmail.pentominto.us.supernotes.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.data.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note : Note)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category : Category)

    @Query("SELECT * FROM note_table")
    fun getAllNotes() : Flow<List<Note>>

}


