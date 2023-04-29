package com.gmail.pentominto.us.supernotes.screens.trashnotescreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.data.Note
import com.gmail.pentominto.us.supernotes.repositories.LocalRepository
import com.gmail.pentominto.us.supernotes.utility.Constants.DEFAULT_CATEGORY
import com.gmail.pentominto.us.supernotes.utility.Constants.TRASH_NOTE_CATEGORY_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class TrashNotesViewModel @Inject constructor(
    val repository: LocalRepository
) : ViewModel() {

    private val _trashNotesList: MutableState<List<Note>> = mutableStateOf(emptyList())
    val trashNotesList: State<List<Note>> = _trashNotesList

    private fun getTrashNotesList() {
        viewModelScope.launch {
            repository.getNotesOfThisCategory(TRASH_NOTE_CATEGORY_TAG).collect { noteList ->

                _trashNotesList.value = noteList
            }
        }
    }

    fun deleteTrashNote(noteId: Int) = viewModelScope.launch {
        repository.deleteNote(noteId)
    }

    fun restoreTrashNote(note: Note) {
        viewModelScope.launch {
            repository.updateNoteCategory(
                DEFAULT_CATEGORY,
                note.noteId
            )
        }
    }

    init {
        getTrashNotesList()
    }
}
