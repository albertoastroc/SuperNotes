package com.gmail.pentominto.us.supernotes.screens.allnotesscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.data.DiscardedNote
import com.gmail.pentominto.us.supernotes.data.SavedNote
import com.gmail.pentominto.us.supernotes.repositories.LocalRepository
import com.gmail.pentominto.us.supernotes.utility.DateGetter.getCurrentDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllNotesViewModel @Inject constructor(
    private val repository : LocalRepository,
    private val dataStore : DataStore<Preferences>
) : ViewModel() {

    private val _allNotesState : MutableState<AllNotesState> = mutableStateOf(AllNotesState())
    val allNotesState : State<AllNotesState> = _allNotesState

    private val hideCategoriesKey = booleanPreferencesKey("hide_categories")
    private val trashEnabled = booleanPreferencesKey("trash_enabled")


    fun onSearchChange(input : String) {

        viewModelScope.launch {

            _allNotesState.value = _allNotesState.value.copy(searchBarInput = input)

            val notesSearchResults = _allNotesState.value.notes.values.flatten().filter { note ->

                note.noteBody.contains(
                    input,
                    true
                )
            }

            _allNotesState.value = _allNotesState.value.copy(notesSearchResults = notesSearchResults)

        }
    }

    fun getNotesList() = viewModelScope.launch {

        repository.getAllCategoriesAndNotes().collect { categoryNotesMap ->

            _allNotesState.value = _allNotesState.value.copy(notes = categoryNotesMap)

        }
    }

    fun deleteNote(noteId : Int) = viewModelScope.launch {

        repository.deleteNote(noteId)
    }

    fun sendToTrash(note : SavedNote) {

        viewModelScope.launch {

            repository.insertTrashNote(

                DiscardedNote(
                    noteTitle = note.noteTitle,
                    noteBody = note.noteBody,
                    createdDate = note.createdDate,
                    lastModified = note.lastModified,
                    dateDeleted = getCurrentDate()

                )
            )
        }
    }

    fun clearSearchBar() {

        _allNotesState.value = _allNotesState.value.copy(searchBarInput = "")
    }

    private fun getPrefs() {

        viewModelScope.launch {

            dataStore.data.collect { preferences ->

                if (preferences.contains(hideCategoriesKey)) {

                    _allNotesState.value = _allNotesState.value.copy(
                        showCategoryTitles = preferences[hideCategoriesKey] ?: true
                    )
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
            getNotesList()
        }
    }
