package com.gmail.pentominto.us.supernotes.repositories

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gmail.pentominto.us.supernotes.data.DiscardedNote
import com.gmail.pentominto.us.supernotes.data.NoteCategory
import com.gmail.pentominto.us.supernotes.data.SavedNote
import kotlinx.coroutines.flow.Flow

interface LocalRepository {

    suspend fun insertNote(note : SavedNote) : Long

    suspend fun insertTrashNote(trashNote : DiscardedNote) : Long

    suspend fun updateNote(noteTitle : String, noteBody : String, noteId : Int, lastModified : String)

    suspend fun updateNoteCategory(chosenCategory : String, noteId : Int)

    fun getTrashNote(id : Int) : Flow<DiscardedNote>

    suspend fun insertCategory(category : NoteCategory)

    suspend fun deleteNote(id : Int)

    suspend fun deleteTrashNote(id : Int)

    suspend fun deleteCategory(id : Int)

    fun getNotesOfThisCategory(category : String) : Flow<List<SavedNote>>

    fun getAllCategoriesAndNotes() : Flow<Map<NoteCategory, List<SavedNote>>>

    fun getAllTrashNotes() : Flow<List<DiscardedNote>>

    fun getNoteWithCategory(id : Int) : Flow<Map<NoteCategory, SavedNote>>

    fun getAllCategories() : Flow<List<NoteCategory>>

    suspend fun deleteAllNotes()

    suspend fun deleteAllTrashNotes()

}