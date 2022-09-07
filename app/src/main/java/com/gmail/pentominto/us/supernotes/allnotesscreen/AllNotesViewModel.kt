package com.gmail.pentominto.us.supernotes.allnotesscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.data.Note
import com.gmail.pentominto.us.supernotes.database.DatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllNotesViewModel @Inject constructor(
    val databaseDao : DatabaseDao
) : ViewModel() {

    private val _notesList : MutableState<List<Note>> = mutableStateOf(emptyList())
    val notesList : State<List<Note>> = _notesList

    private val _categories : MutableState<List<Category>> = mutableStateOf(emptyList())
    val categories : State<List<Category>> = _categories

    fun getNotes() = viewModelScope.launch {

        databaseDao.getAllNotes().collect() {
            _notesList.value = it
        }
    }

    fun getCategories() = viewModelScope.launch {

        databaseDao.getAllCategories().collect() {

            _categories.value = it
        }

    }

    init {
        getNotes()
        getCategories()
    }
}