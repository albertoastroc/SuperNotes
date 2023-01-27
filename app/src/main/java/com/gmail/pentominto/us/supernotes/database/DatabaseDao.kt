package com.gmail.pentominto.us.supernotes.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gmail.pentominto.us.supernotes.data.entities.Category
import com.gmail.pentominto.us.supernotes.data.entities.Note
import com.gmail.pentominto.us.supernotes.data.entities.TrashNote
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note : Note) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrashNote(trashNote : TrashNote) : Long

    @Query("UPDATE note_table SET noteTitle = :noteTitle, noteBody = :noteBody, lastModified = :lastModified WHERE note_db_id = :noteId")
    suspend fun updateNote(noteTitle : String, noteBody : String, noteId : Int, lastModified : String)

    @Query("UPDATE note_table SET category = :chosenCategory WHERE note_db_id = :noteId")
    suspend fun updateNoteCategory(chosenCategory : String, noteId : Int)

    @Query("SELECT * FROM note_table WHERE note_db_id = :id")
    fun getNote(id : Int) : Flow<Note>

    @Query("SELECT * FROM trash_note_table WHERE trash_note_db_id = :id")
    fun getTrashNote(id : Int) : Flow<TrashNote>

    @Query("SELECT EXISTS(SELECT * FROM category_table WHERE categoryTitle = 'No Category')")
    suspend fun defaultCategoryExists() : Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category : Category)

    @Query("SELECT * FROM note_table")
    fun getAllNotes() : Flow<List<Note>>

    @Query("DELETE FROM note_table WHERE note_db_id = :id")
    suspend fun deleteNote(id : Int)

    @Query("DELETE FROM trash_note_table WHERE trash_note_db_id = :id")
    suspend fun deleteTrashNote(id : Int)

    @Query("DELETE FROM category_table WHERE category_db_id = :id")
    suspend fun deleteCategory(id : Int)

    @Query("SELECT * FROM note_table WHERE category = :category")
    fun getNotesOfThisCategory(category : String) : Flow<List<Note>>

    @Query("SELECT * FROM note_table JOIN category_table ON note_table.category = category_table.categoryTitle")
    fun getAllCategoriesAndNotes() : Flow<Map<Category, List<Note>>>

    @Query("SELECT * FROM trash_note_table")
    fun getAllTrashNotes() : Flow<List<TrashNote>>

    @Query("SELECT * FROM note_table JOIN category_table ON note_table.category = category_table.categoryTitle WHERE note_db_id = :id")
    fun getNoteWithCategory(id : Int) : Flow<Map<Category, Note>>

    @Query("SELECT * FROM category_table")
    fun getAllCategories() : Flow<List<Category>>

    @Query("DELETE FROM note_table ")
    suspend fun deleteAllNotes()

    @Query("DELETE FROM trash_note_table ")
    suspend fun deleteAllTrashNotes()
}


