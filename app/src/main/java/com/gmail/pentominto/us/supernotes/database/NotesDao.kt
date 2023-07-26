package com.gmail.pentominto.us.supernotes.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.data.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note): Long

    @Query(
        "UPDATE note_table SET noteTitle = :noteTitle, noteBody = :noteBody WHERE note_db_id = :noteId"
    )
    suspend fun updateNote(noteTitle: String, noteBody: String, noteId: Int)

    @Query("UPDATE note_table SET category = :chosenCategory WHERE note_db_id = :noteId")
    suspend fun updateNoteCategory(chosenCategory: String, noteId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Query("DELETE FROM note_table WHERE note_db_id = :id")
    suspend fun deleteNote(id: Int)

    @Query("DELETE FROM category_table WHERE category_db_id = :id")
    suspend fun deleteCategory(id: Int)

    @Query("SELECT * FROM note_table WHERE category = :category")
    fun getNotesOfThisCategory(category: String): Flow<List<Note>>

    @Query(
        "SELECT * FROM note_table JOIN category_table ON note_table.category = category_table.categoryTitle ORDER BY categoryTitle COLLATE NOCASE ASC"
    )
    fun getAllCategoriesAndNotes(): Flow<Map<Category, List<Note>>>

    @Query(
        "SELECT * FROM note_table JOIN category_table ON note_table.category = category_table.categoryTitle WHERE note_db_id = :id"
    )
    fun getNoteWithCategory(id: Int): Flow<Map<Category, Note>>

    @Query("SELECT * FROM note_table WHERE note_db_id = :id")
    fun getNote(id: Int): Flow<Note>

    @Query("SELECT * FROM category_table")
    fun getAllCategories(): Flow<List<Category>>

    @Query("DELETE FROM note_table WHERE category != 'TrashNotesAPPTAG' ")
    suspend fun deleteHomeScreenNotes()

    @Query("DELETE FROM note_table WHERE category = 'TrashNotesAPPTAG' ")
    suspend fun deleteAllTrashNotes()

    // temporary while prepoluated database gets fixed
    @Query("SELECT EXISTS(SELECT * FROM category_table WHERE categoryTitle = 'No Category')")
    suspend fun defaultCategoryExists(): Boolean
}
