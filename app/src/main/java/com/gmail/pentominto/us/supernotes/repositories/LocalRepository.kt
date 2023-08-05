package com.gmail.pentominto.us.supernotes.repositories

import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.data.Note
import kotlinx.coroutines.flow.Flow

interface LocalRepository {

    suspend fun insertNote(note: Note): Long

    suspend fun updateNote(noteTitle: String, noteBody: String, noteId: Int)

    suspend fun updateNoteCategory(chosenCategory: String, noteId: Int)

    suspend fun insertCategory(category: Category)

    suspend fun deleteNote(id: Int)

    suspend fun deleteCategory(id: Int)

    fun getNotesOfThisCategory(category: String): Flow<List<Note>>

    fun getAllCategoriesAndNotes(): Flow<Map<Category, List<Note>>>

    fun getNoteWithItsCategory(id: Int): Flow<Map<Category, Note>>

    fun getNote(id: Int): Flow<Note>

    fun getCategories(): Flow<List<Category>>

    suspend fun deleteHomeScreenNotes()

    suspend fun deleteTrashNotes()

    suspend fun defaultCategoryExists(): Boolean
}
