package com.gmail.pentominto.us.supernotes.repositories

import com.gmail.pentominto.us.supernotes.data.DiscardedNote
import com.gmail.pentominto.us.supernotes.data.NoteCategory
import com.gmail.pentominto.us.supernotes.data.SavedNote
import com.gmail.pentominto.us.supernotes.database.DatabaseDAO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepositoryImpl @Inject constructor(
    private val dao : DatabaseDAO
) : LocalRepository {

    override suspend fun insertNote(note : SavedNote) : Long {

        return dao.insertNote(note)
    }

    override suspend fun insertTrashNote(trashNote : DiscardedNote) : Long {

        return dao.insertTrashNote(trashNote)

    }

    override suspend fun updateNote(noteTitle : String, noteBody : String, noteId : Int, lastModified : String) {

       dao.updateNote(noteTitle, noteBody, noteId, lastModified)

    }

    override suspend fun updateNoteCategory(chosenCategory : String, noteId : Int) {
        dao.updateNoteCategory(chosenCategory, noteId)
    }

    override fun getTrashNote(id : Int) : Flow<DiscardedNote> {

        return dao.getTrashNote(id)

    }

    override suspend fun insertCategory(category : NoteCategory) {

        dao.insertCategory(category)

    }

    override suspend fun deleteNote(id : Int) {

        dao.deleteNote(id)

    }

    override suspend fun deleteTrashNote(id : Int) {

        dao.deleteTrashNote(id)

    }

    override suspend fun deleteCategory(id : Int) {

        dao.deleteCategory(id)

    }

    override fun getNotesOfThisCategory(category : String) : Flow<List<SavedNote>> {

        return dao.getNotesOfThisCategory(category)

    }

    override fun getAllCategoriesAndNotes() : Flow<Map<NoteCategory, List<SavedNote>>> {

        return dao.getAllCategoriesAndNotes()

    }

    override fun getAllTrashNotes() : Flow<List<DiscardedNote>> {

        return dao.getAllTrashNotes()

    }

    override fun getNoteWithCategory(id : Int) : Flow<Map<NoteCategory, SavedNote>> {

        return dao.getNoteWithCategory(id)

    }

    override fun getAllCategories() : Flow<List<NoteCategory>> {

        return dao.getAllCategories()

    }

    override suspend fun deleteAllNotes() {

        dao.deleteAllNotes()

    }

    override suspend fun deleteAllTrashNotes() {

        dao.deleteAllTrashNotes()

    }
}