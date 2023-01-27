package com.gmail.pentominto.us.supernotes.screens.readonlynotescreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.database.DatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadOnlyNoteViewModel @Inject constructor(
    private val databaseDao : DatabaseDao,
    savedStateHandle : SavedStateHandle
) : ViewModel() {

    private val trashNoteId : Int = checkNotNull(savedStateHandle["trashNoteId"])

    private val _readOnlyNoteState : MutableState<ReadOnlyNoteScreenState> = mutableStateOf(ReadOnlyNoteScreenState())
    val readOnlyNoteState : State<ReadOnlyNoteScreenState> = _readOnlyNoteState

    private fun getTrashNote(trashNoteId : Int) {

        viewModelScope.launch {

            databaseDao.getTrashNote(trashNoteId).collect { trashNote ->

                _readOnlyNoteState.value = _readOnlyNoteState.value.copy(
                    trashNote = trashNote
                )
            }
        }
    }

    init {
        getTrashNote(trashNoteId)
    }
}