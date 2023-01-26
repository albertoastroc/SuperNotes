package com.gmail.pentominto.us.supernotes.screens.noteeditscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.data.entities.CategoryEntity
import com.gmail.pentominto.us.supernotes.data.entities.NoteEntity
import com.gmail.pentominto.us.supernotes.database.DatabaseDao
import com.gmail.pentominto.us.supernotes.utility.DateGetter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditScreenViewModel @Inject constructor(
    private val databaseDao : DatabaseDao,
    savedStateHandle : SavedStateHandle
) : ViewModel() {

    private val noteId : Long? = savedStateHandle["noteId"]

    private val _noteEditState : MutableState<NoteEditState> = mutableStateOf(NoteEditState())
    val noteEditState : State<NoteEditState> = _noteEditState

    private fun getNote(noteId : Long) {

        if (noteId == 0L) {

            insertNewNote()
        } else {

            viewModelScope.launch {

                databaseDao.getNoteWithCategory(noteId).collect { categoryNoteMap ->

                    categoryNoteMap.forEach {

                        _noteEditState.value = _noteEditState.value.copy(
                            noteCategory = it.key,
                            note = it.value,
                            noteTitle = it.value.noteTitle,
                            noteBody = it.value.noteBody
                        )
                    }
                }
            }
        }
    }

    private fun insertNewNote() {

        viewModelScope.launch {

            if (! databaseDao.defaultCategoryExists()) {

                insertCategory("No CategoryEntity")
            }

            getNote(
                databaseDao.insertNote(
                    NoteEntity(
                        category = "No CategoryEntity",
                        createdDate = noteEditState.value.currentDate,
                        lastModified = noteEditState.value.currentDate,
                        noteBody = noteEditState.value.noteBody,
                        noteTitle = noteEditState.value.noteTitle
                    )
                )
            )
        }
    }

    private fun getCategories() {

        viewModelScope.launch {

            databaseDao.getAllCategories().collect {

                _noteEditState.value = _noteEditState.value.copy(categories = it)
            }
        }
    }

    private fun getCurrentDate() {

        _noteEditState.value = _noteEditState.value.copy(
            currentDate = DateGetter.getCurrentDate()
        )
    }

    fun insertCategory(categoryName : String) {

        viewModelScope.launch {

            saveNoteText()
            databaseDao.insertCategory(CategoryEntity(categoryName))
        }
    }

    fun deleteCategory(category : CategoryEntity) {

        viewModelScope.launch {

            val notesToUpdate = databaseDao.getNotesOfThisCategory(category.categoryTitle)

            databaseDao.deleteCategory(category.categoryId)

            notesToUpdate.collect { notesList ->

                notesList.forEach { note ->

                    databaseDao.updateNoteCategory(
                        "No CategoryEntity",
                        note.noteId
                    )
                }
            }
        }
    }

    fun saveNoteText() {

        viewModelScope.launch {
            databaseDao.updateNote(
                noteTitle = _noteEditState.value.noteTitle.toString(),
                noteBody = _noteEditState.value.noteBody.toString(),
                noteId = _noteEditState.value.note.noteId,
                lastModified = _noteEditState.value.currentDate
            )
        }
    }

    fun saveNoteCategory(category : CategoryEntity) {

        viewModelScope.launch {

            saveNoteText()
            databaseDao.updateNoteCategory(
                chosenCategory = category.categoryTitle,
                noteId = _noteEditState.value.note.noteId
            )
        }
    }

    fun onTitleInputChange(newInput : String) {

        _noteEditState.value = _noteEditState.value.copy(
            noteTitle = newInput
        )
    }

    fun onBodyInputChange(newInput : String) {

        _noteEditState.value = _noteEditState.value.copy(
            noteBody = newInput
        )
    }

    init {
        if (noteId != null) {
            getNote(noteId)
            getCategories()
            getCurrentDate()
        }
    }
}