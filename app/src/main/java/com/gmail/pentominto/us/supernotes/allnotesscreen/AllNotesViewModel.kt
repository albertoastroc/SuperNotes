package com.gmail.pentominto.us.supernotes.allnotesscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
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
    val databaseDao : DatabaseDao,
    private val dataStore : DataStore<Preferences>
) : ViewModel() {

    private val _notesListWithCategories : MutableState<Map<Category, List<Note>>> = mutableStateOf(emptyMap())
    val notesListWithCategories : State<Map<Category, List<Note>>> = _notesListWithCategories

    private val _notesListNoCategories : MutableState<List<Note>> = mutableStateOf(emptyList())
    val notesListNoCategories : State<List<Note>> = _notesListNoCategories

    private val _notesListSearchResults : MutableState<List<Note>> = mutableStateOf(emptyList())
    val notesListSearchResults : State<List<Note>> = _notesListSearchResults

    private val _categories : MutableState<List<Category>> = mutableStateOf(emptyList())
    val categories : State<List<Category>> = _categories

    private val _searchBarText : MutableState<String> = mutableStateOf(String())
    val searchBarText : State<String> = _searchBarText

    private val _categoriesOptionState : MutableState<Boolean> = mutableStateOf(false)
    val categoriesOptionsState : State<Boolean> = _categoriesOptionState

    val hideCategoriesKey = booleanPreferencesKey("hide_categories")

    fun onSearchChange(input : String) {

        _searchBarText.value = input

        viewModelScope.launch {

            _notesListSearchResults.value = notesListNoCategories.value.filter { note ->

                note.noteBody?.contains(input, true) ?: false
            }
        }
    }

    fun getNotesWithCategories() = viewModelScope.launch {

        databaseDao.getAllCategoriesAndNotes().collect() {
            _notesListWithCategories.value = it
        }
    }

    fun deleteNote(noteId : Long) = viewModelScope.launch {

        databaseDao.deleteNote(noteId)
    }

    fun getNotesNoCategories() = viewModelScope.launch {

        databaseDao.getAllNotes().collect() {
            _notesListNoCategories.value = it
        }

    }

    fun getPrefs() {

        viewModelScope.launch {

            dataStore.data.collect() { preferences ->

                if (preferences.contains(hideCategoriesKey)) {

                    _categoriesOptionState.value = preferences[hideCategoriesKey] !!
                }
            }
        }
    }

    init {
        getPrefs()
    }
}
