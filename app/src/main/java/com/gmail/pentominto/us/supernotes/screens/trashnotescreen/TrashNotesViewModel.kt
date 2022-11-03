package com.gmail.pentominto.us.supernotes.screens.trashnotescreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.data.TrashNote
import com.gmail.pentominto.us.supernotes.database.DatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrashNotesViewModel @Inject constructor(
    val databaseDao : DatabaseDao
) : ViewModel() {

    private val _trashNotesList : MutableState<List<TrashNote>> = mutableStateOf(emptyList())
    val trashNotesList : State<List<TrashNote>> = _trashNotesList

    fun getTrashNotesList(){

        viewModelScope.launch {

            databaseDao.getAllTrashNotes().collect() { noteList ->

                _trashNotesList.value = noteList
            }
        }
    }

    init {

        getTrashNotesList()
    }
}