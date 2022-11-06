package com.gmail.pentominto.us.supernotes.screens.noteeditscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.data.Note
import com.gmail.pentominto.us.supernotes.database.DatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
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

                databaseDao.getNoteWithCategory(noteId).collect() { categoryNoteMap ->

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

                insertCategory("No Category")
            }

            getNote(
                databaseDao.insertNote(
                    Note(
                        category = "No Category",
                        createdDate = noteEditState.value.currentDate,
                        lastModified = noteEditState.value.currentDate
                    )
                )
            )
        }
    }

    private fun getCategories() {

        viewModelScope.launch {

            databaseDao.getAllCategories().collect() {

                _noteEditState.value = _noteEditState.value.copy(categories = it)
            }
        }
    }

    private fun getCurrentDate() {

        val currentTime = Calendar.getInstance().time
        val dateFormatter = SimpleDateFormat("M/d/yy")
        _noteEditState.value = _noteEditState.value.copy(
            currentDate = dateFormatter.format(currentTime)
        )
    }

    fun insertCategory(categoryName : String) {

        viewModelScope.launch {

            saveNoteText()
            databaseDao.insertCategory(Category(categoryName))
        }
    }

    fun deleteCategory(category : Category) {

        viewModelScope.launch {

            val notesToUpdate = databaseDao.getNotesOfThisCategory(category.categoryTitle)

            databaseDao.deleteCategory(category.categoryId)

            notesToUpdate.collect() { notesList ->

                notesList.forEach { note ->

                    databaseDao.updateNoteCategory(
                        "No Category",
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

    fun saveNoteCategory(category : Category) {

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