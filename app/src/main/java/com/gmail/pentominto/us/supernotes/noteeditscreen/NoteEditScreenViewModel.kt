package com.gmail.pentominto.us.supernotes.noteeditscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.data.Note
import com.gmail.pentominto.us.supernotes.database.DatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditScreenViewModel @Inject constructor(
    val databaseDao : DatabaseDao
) : ViewModel() {

    private var inputJob : Job? = null

    private val _noteState : MutableState<Note> = mutableStateOf(Note("", ""))
    val noteState : State<Note> = _noteState

    fun getNote(noteId : Long) {

        viewModelScope.launch {

            databaseDao.getNote(noteId).collect() {

                _noteState.value = it
            }

        }

    }

    fun updateNote(id : Long) {

        viewModelScope.launch {
            databaseDao.insertNote(
                Note(
                    noteTitle = _noteState.value.noteTitle,
                    noteBody = _noteState.value.noteBody,
                    noteId = id
                )
            )
        }

    }

    fun onTitleInputChange(newInput : String) {

        inputJob?.cancel()
        inputJob = viewModelScope.launch {

            _noteState.value = _noteState.value.copy(
                noteTitle = newInput
            )
        }
    }

    fun onBodyInputChange(newInput : String) {

        inputJob?.cancel()
        inputJob = viewModelScope.launch {

            _noteState.value = _noteState.value.copy(
                noteBody = newInput
            )
        }
    }

}