package com.gmail.pentominto.us.supernotes.noteeditscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.data.Note
import com.gmail.pentominto.us.supernotes.database.DatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditScreenViewModel @Inject constructor(
    private val databaseDao : DatabaseDao
) : ViewModel() {

    private val _note : MutableState<Note> = mutableStateOf(
        Note(
            "",
            "",
            ""
        )
    )
    val note : State<Note> = _note

    private val _categories : MutableState<List<Category>> = mutableStateOf(emptyList())

    val categories : State<List<Category>> = _categories

    fun getNote(noteId : Long) {

        viewModelScope.launch {

            databaseDao.getNote(noteId).collect() {

                _note.value = it
            }
        }

        viewModelScope.launch {

            databaseDao.getAllCategories().collect() {

                _categories.value = it
            }

        }
    }

    fun insertNewNote() {

        viewModelScope.launch {

            var newNoteId = 0L
            newNoteId = databaseDao.insertNote(
                Note(
                    "",
                    "",
                    ""
                )
            )
            getNote(newNoteId)
        }
    }

    fun updateNote() {

        viewModelScope.launch {
            databaseDao.updateNote(
                noteTitle = _note.value.noteTitle,
                noteBody = _note.value.noteBody,
                noteId = note.value.noteId

            )
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
}