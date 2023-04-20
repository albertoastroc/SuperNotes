package com.gmail.pentominto.us.supernotes.screens.readonlynotescreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.repositories.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ReadOnlyNoteViewModel @Inject constructor(
    private val repository: LocalRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val trashNoteId: Int = checkNotNull(savedStateHandle["trashNoteId"])

    private val _readOnlyNoteState: MutableState<ReadOnlyNoteScreenState> = mutableStateOf(
        ReadOnlyNoteScreenState()
    )
    val readOnlyNoteState: State<ReadOnlyNoteScreenState> = _readOnlyNoteState

    private fun getTrashNote(trashNoteId: Int) {
        viewModelScope.launch {
            repository.getNote(trashNoteId).collect { trashNote ->

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
