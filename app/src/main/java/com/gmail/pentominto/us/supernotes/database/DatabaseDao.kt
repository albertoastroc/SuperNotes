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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note : Note) : Long

    @Query("UPDATE note_table SET noteTitle = :noteTitle, noteBody = :noteBody WHERE note_db_id = :noteId")
    suspend fun updateNote(noteTitle : String, noteBody : String, noteId : Long)

    @Query("SELECT * FROM note_table WHERE note_db_id = :id")
    fun getNote(id : Long) : Flow<Note>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category : Category)

    @Query("SELECT * FROM note_table")
    fun getAllNotes() : Flow<List<Note>>

    @Query("SELECT * FROM category_table")
    fun getAllCategories() : Flow<List<Category>>

}


