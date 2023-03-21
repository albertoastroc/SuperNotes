package com.gmail.pentominto.us.supernotes.screens.trashnotescreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.data.DiscardedNote
import com.gmail.pentominto.us.supernotes.data.SavedNote
import com.gmail.pentominto.us.supernotes.repositories.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class TrashNotesViewModel @Inject constructor(
    val repository: LocalRepository
) : ViewModel() {

    private val _trashNotesList: MutableState<List<DiscardedNote>> = mutableStateOf(emptyList())
    val trashNotesList: State<List<DiscardedNote>> = _trashNotesList

    private fun getTrashNotesList() {
        viewModelScope.launch {
            repository.getAllTrashNotes().collect { noteList ->

                _trashNotesList.value = noteList
            }
        }
    }

    fun deleteTrashNote(noteId: Int) = viewModelScope.launch {
        repository.deleteTrashNote(noteId)
    }

    fun restoreTrashNote(note: DiscardedNote) {
        viewModelScope.launch {
            repository.insertNote(
                SavedNote(
                    noteTitle = note.noteTitle,
                    noteBody = note.noteBody,
                    createdDate = note.createdDate
                )
            )
            deleteTrashNote(note.noteId)
        }
    }

    init {

        getTrashNotesList()
    }
}
