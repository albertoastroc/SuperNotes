package com.gmail.pentominto.us.supernotes.allnotesscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.data.Note
import com.gmail.pentominto.us.supernotes.database.DatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllNotesViewModel @Inject constructor(
    val databaseDao : DatabaseDao
) : ViewModel() {

    val notesList  : MutableState<List<Note>> = mutableStateOf(emptyList())

       fun getNotes() = viewModelScope.launch {

           databaseDao.getAllNotes().collect() {
               notesList.value = it
           }
       }

    init {
        getNotes()
    }
}