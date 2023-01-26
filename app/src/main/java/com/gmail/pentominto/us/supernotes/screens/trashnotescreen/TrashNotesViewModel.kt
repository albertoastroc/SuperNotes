package com.gmail.pentominto.us.supernotes.screens.trashnotescreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.data.entities.NoteEntity
import com.gmail.pentominto.us.supernotes.data.entities.TrashNoteEntity
import com.gmail.pentominto.us.supernotes.database.DatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrashNotesViewModel @Inject constructor(
    val databaseDao : DatabaseDao
) : ViewModel() {

    private val _trashNotesList : MutableState<List<TrashNoteEntity>> = mutableStateOf(emptyList())
    val trashNotesList : State<List<TrashNoteEntity>> = _trashNotesList

    private fun getTrashNotesList() {

        viewModelScope.launch {

            databaseDao.getAllTrashNotes().collect { noteList ->

                _trashNotesList.value = noteList
            }
        }
    }

    fun deleteTrashNote(noteId : Long) = viewModelScope.launch {

        databaseDao.deleteTrashNote(noteId)
    }

    fun restoreTrashNote(note : TrashNoteEntity) {

        viewModelScope.launch {

            databaseDao.insertNote(
                NoteEntity(
                    noteTitle = note.noteTitle,
                    noteBody = note.noteBody,
                    category = note.category,
                    createdDate = note.createdDate,
                    lastModified = note.lastModified
                )
            )
            deleteTrashNote(note.noteId)
        }
    }

    init {

        getTrashNotesList()
    }
}