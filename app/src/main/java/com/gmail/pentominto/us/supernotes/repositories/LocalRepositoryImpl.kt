package com.gmail.pentominto.us.supernotes.repositories

import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.data.Note
import com.gmail.pentominto.us.supernotes.database.NotesDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepositoryImpl @Inject constructor(
    private val dao: NotesDao
) : LocalRepository {

    override suspend fun insertNote(note: Note): Long {
        return dao.insertNote(note)
    }

    override suspend fun insertTrashNote(trashNote: Note): Long {
        return dao.insertNote(trashNote)
    }

    override suspend fun updateNote(
        noteTitle: String,
        noteBody: String,
        noteId: Int
    ) {
        dao.updateNote(
            noteTitle,
            noteBody,
            noteId
        )
    }

    override suspend fun updateNoteCategory(chosenCategory: String, noteId: Int) {
        dao.updateNoteCategory(
            chosenCategory,
            noteId
        )
    }

    override suspend fun insertCategory(category: Category) {
        dao.insertCategory(category)
    }

    override suspend fun deleteNote(id: Int) {
        dao.deleteNote(id)
    }

    override suspend fun deleteTrashNote(id: Int) {
        dao.deleteNote(id)
    }

    override suspend fun deleteCategory(id: Int) {
        dao.deleteCategory(id)
    }

    override fun getNotesOfThisCategory(category: String): Flow<List<Note>> {
        return dao.getNotesOfThisCategory(category)
    }

    override fun getAllCategoriesAndNotes(): Flow<Map<Category, List<Note>>> {
        return dao.getAllCategoriesAndNotes()
    }

    override fun getNoteWithCategory(id: Int): Flow<Map<Category, Note>> {
        return dao.getNoteWithCategory(id)
    }

    override fun getNote(id: Int): Flow<Note> {
        return dao.getNote(id)
    }

    override fun getAllCategories(): Flow<List<Category>> {
        return dao.getAllCategories()
    }

    override suspend fun deleteHomeScreenNotes() {
        dao.deleteHomeScreenNotes()
    }

    override suspend fun deleteAllTrashNotes() {
        dao.deleteAllTrashNotes()
    }

    override suspend fun defaultCategoryExists(): Boolean {
        return dao.defaultCategoryExists()
    }
}
