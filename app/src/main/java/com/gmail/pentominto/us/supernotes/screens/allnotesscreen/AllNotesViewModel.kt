package com.gmail.pentominto.us.supernotes.screens.allnotesscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.database.DatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllNotesViewModel @Inject constructor(
    val databaseDao : DatabaseDao,
    private val dataStore : DataStore<Preferences>
) : ViewModel() {

    private val _allNotesState : MutableState<AllNotesState> = mutableStateOf(AllNotesState())
    val allNotesState : State<AllNotesState> = _allNotesState

    val hideCategoriesKey = booleanPreferencesKey("hide_categories")

    fun onSearchChange(input : String) {

        viewModelScope.launch {

            _allNotesState.value = _allNotesState.value.copy(searchBarInput = input, currentList = CurrentList.SEARCH_RESULTS)

            val notesSearchResults = _allNotesState.value.notesWithNoCategories.filter { note ->

                note.noteBody?.contains(
                    input,
                    true
                ) ?: false
            }

            _allNotesState.value = _allNotesState.value.copy(notesSearchResults = notesSearchResults)

        }
    }

    fun getNotesList() = viewModelScope.launch {

        databaseDao.getAllCategoriesAndNotes().collect() { categoryNotesMap ->

            _allNotesState.value = _allNotesState.value.copy(notesWithCategory = categoryNotesMap)

            _allNotesState.value = _allNotesState.value.copy(notesWithNoCategories = categoryNotesMap.values.flatten())

        }
    }

    fun deleteNote(noteId : Long) = viewModelScope.launch {

        databaseDao.deleteNote(noteId)
    }

    fun getPrefs() {

        viewModelScope.launch {

            dataStore.data.collect() { preferences ->

                if (preferences.contains(hideCategoriesKey)) {

                    _allNotesState.value = _allNotesState.value.copy(
                        showCategories = preferences[hideCategoriesKey] ?: true
                    )

                    if (_allNotesState.value.showCategories){
                        _allNotesState.value = _allNotesState.value.copy(currentList = CurrentList.CATEGORIES)
                    } else _allNotesState.value = _allNotesState.value.copy(currentList = CurrentList.NO_CATEGORIES)
                }
            }
        }
    }

    init {
        getPrefs()
    }
}
