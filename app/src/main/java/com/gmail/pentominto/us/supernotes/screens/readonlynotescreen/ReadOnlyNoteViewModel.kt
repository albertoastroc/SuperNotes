package com.gmail.pentominto.us.supernotes.screens.readonlynotescreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.data.entities.TrashNote
import com.gmail.pentominto.us.supernotes.database.DatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadOnlyNoteViewModel @Inject constructor(
    private val databaseDao : DatabaseDao,
    savedStateHandle : SavedStateHandle
) : ViewModel() {

    private val trashNoteId : Long? = savedStateHandle["trashNoteId"]

    private val _trashNoteState : MutableState<TrashNote> = mutableStateOf(TrashNote())
    val trashNoteState : State<TrashNote> = _trashNoteState

    private fun getTrashNote(trashNoteId : Int) {

        viewModelScope.launch {

            databaseDao.getTrashNote(trashNoteId).collect { trashNote ->

                _trashNoteState.value = trashNote
            }
        }
    }

    init {
        if (trashNoteId != null) {
            getTrashNote(trashNoteId)
        }
    }
}