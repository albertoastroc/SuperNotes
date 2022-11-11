package com.gmail.pentominto.us.supernotes.screens.allnotesscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.data.Note
import com.gmail.pentominto.us.supernotes.data.TrashNote
import com.gmail.pentominto.us.supernotes.database.DatabaseDao
import com.gmail.pentominto.us.supernotes.utility.Constants.HIDE_CATEGORIES_KEY
import com.gmail.pentominto.us.supernotes.utility.Constants.TRASH_ENABLED_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AllNotesViewModel @Inject constructor(
    val databaseDao : DatabaseDao,
    private val dataStore : DataStore<Preferences>
) : ViewModel() {

    private val _allNotesState : MutableState<AllNotesState> = mutableStateOf(AllNotesState())
    val allNotesState : State<AllNotesState> = _allNotesState

    private val hideCategoriesKey = booleanPreferencesKey(HIDE_CATEGORIES_KEY)
    private val trashEnabled = booleanPreferencesKey(TRASH_ENABLED_KEY)

    fun onSearchChange(input : String) {

        viewModelScope.launch {

            _allNotesState.value = _allNotesState.value.copy(searchBarInput = input)

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

    fun sendToTrash(note : Note) {

        viewModelScope.launch {

            databaseDao.insertTrashNote(
                TrashNote(
                    noteTitle = note.noteTitle,
                    noteBody = note.noteBody,
                    category = note.category,
                    createdDate = note.createdDate,
                    lastModified = note.lastModified,
                    dateDeleted = allNotesState.value.currentDate
                )
            )
        }
    }

    private fun getCurrentDate() {

        val currentTime = Calendar.getInstance().time
        val dateFormatter = SimpleDateFormat("M/d/yy")
        _allNotesState.value = _allNotesState.value.copy(
            currentDate = dateFormatter.format(currentTime)
        )
    }

    fun clearSearchBar() {

        _allNotesState.value = _allNotesState.value.copy(searchBarInput = String())
    }

    fun getPrefs() {

        viewModelScope.launch {

            dataStore.data.collect() { preferences ->

                if (preferences.contains(hideCategoriesKey)) {

                    _allNotesState.value = _allNotesState.value.copy(
                        showCategories = preferences[hideCategoriesKey] ?: true
                    )

                    if (_allNotesState.value.showCategories) {
                        _allNotesState.value = _allNotesState.value.copy(currentList = CurrentList.WITH_CATEGORIES)
                    } else _allNotesState.value = _allNotesState.value.copy(currentList = CurrentList.WITHOUT_CATEGORIES)
                }

                if (preferences.contains(trashEnabled)) {

                    _allNotesState.value = _allNotesState.value.copy(
                        trashEnabled = preferences[trashEnabled] ?: true
                    )
                }
            }
        }
    }

    init {
        getPrefs()
        getCurrentDate()
    }
}
