package com.gmail.pentominto.us.supernotes.noteeditscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.data.Note
import com.gmail.pentominto.us.supernotes.database.DatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditScreenViewModel @Inject constructor(
    val databaseDao : DatabaseDao
) : ViewModel() {

    fun saveNote(noteTitle : String) {

        viewModelScope.launch {
            databaseDao.insertNote(Note(noteTitle))
        }

    }

}