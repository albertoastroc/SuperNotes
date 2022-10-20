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

    val noteId : Long? = savedStateHandle["noteId"]

    private val _note : MutableState<Note> = mutableStateOf(Note())
    val note : State<Note> = _note

    private val _categories : MutableState<List<Category>> = mutableStateOf(emptyList())
    val categories : State<List<Category>> = _categories

    private val _noteCategory : MutableState<Category> = mutableStateOf(Category())
    val noteCategory : State<Category> = _noteCategory

    private val _currentDate : MutableState<String> = mutableStateOf(String())
    val currentDate : State<String> = _currentDate

    fun getNote(noteId : Long){

        if (noteId == 0L){

            insertNewNote()

        } else {

            viewModelScope.launch {

                databaseDao.getNoteWithCategory(noteId).collect() {

                    it.forEach {

                        _noteCategory.value = it.key
                        _note.value = it.value
                    }
                }
            }
        }
    }

    fun insertNewNote() {

        viewModelScope.launch {

            if (! databaseDao.defaultCategoryExists()) {

                insertCategory("No Category")
            }

            getNote(
                databaseDao.insertNote(Note(
                    category = "No Category",
                    createdDate = currentDate.value,
                    lastModified = currentDate.value
                ))
            )
        }
    }

    fun getCategories() {

        viewModelScope.launch {

            databaseDao.getAllCategories().collect() {

                _categories.value = it
            }
        }
    }

    fun insertCategory(categoryName : String) {

        viewModelScope.launch {

            saveNoteText()
            databaseDao.insertCategory(Category(categoryName))
        }
    }

    fun deleteCategory(category : Category) {

        viewModelScope.launch {

            val categoriesToUpdate = databaseDao.getNotesOfThisCategory(category.categoryTitle)

            databaseDao.deleteCategory(category.categoryId)

            categoriesToUpdate.collect() {

                it.forEach {

                    databaseDao.updateNoteCategory("No Category", it.noteId)
                }
            }
        }
    }

    fun saveNoteText() {

        viewModelScope.launch {
            databaseDao.updateNote(
                noteTitle = _note.value.noteTitle.toString(),
                noteBody = _note.value.noteBody.toString(),
                noteId = note.value.noteId,
                lastModified = currentDate.value
            )
        }
    }

    fun saveNoteCategory(category : Category) {

        viewModelScope.launch {

            saveNoteText()
            _noteCategory.value.let {
                databaseDao.updateNoteCategory(
                    chosenCategory = category.categoryTitle,
                    noteId = note.value.noteId
                )
            }
        }
    }

    fun onTitleInputChange(newInput : String) {

        _note.value = _note.value.copy(
            noteTitle = newInput
        )
    }

    fun onBodyInputChange(newInput : String) {

        _note.value = _note.value.copy(
            noteBody = newInput
        )
    }

    fun getCurrentDate() {

        val currentTime = Calendar.getInstance().time
        val dateFormatter = SimpleDateFormat("M/d/yy")
        _currentDate.value = dateFormatter.format(currentTime)
    }

    init {
        if (noteId != null) {
            getNote(noteId)
        }
        getCategories()
        getCurrentDate()
    }

}