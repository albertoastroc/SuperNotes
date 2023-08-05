package com.gmail.pentominto.us.supernotes.screens.homescreennotesscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.data.Note
import com.gmail.pentominto.us.supernotes.repositories.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenNotesViewModel @Inject constructor(
    private val repository: LocalRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _homeScreenNotesState: MutableState<HomeScreenNotesState> = mutableStateOf(HomeScreenNotesState())
    val homeScreenNotesState: State<HomeScreenNotesState> = _homeScreenNotesState

    private val hideCategoriesKey = booleanPreferencesKey("hide_categories")
    private val isTrashEnabledKey = booleanPreferencesKey("trash_enabled")

    fun onSearchChange(input: String) {
        viewModelScope.launch {
            _homeScreenNotesState.value = _homeScreenNotesState.value.copy(searchBarInput = input.lowercase())
        }
    }
    private fun showCategories() {
        if (_homeScreenNotesState.value.showCategoryTitles && homeScreenNotesState.value.searchBarInput.isEmpty()) {
            _homeScreenNotesState.value = _homeScreenNotesState.value.copy(showCategoryTitles = true)
        } else { _homeScreenNotesState.value = _homeScreenNotesState.value.copy(showCategoryTitles = false) }
    }

    private fun getNotesList() = viewModelScope.launch {
        repository.getAllCategoriesAndNotes().collect { categoryNotesMap ->

            _homeScreenNotesState.value = _homeScreenNotesState.value.copy(notes = categoryNotesMap)
        }
    }

    private fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note.noteId)
        }
    }

    private fun sendToTrash(note: Note) {
        viewModelScope.launch {
            repository.updateNoteCategory(
                "TrashNotesAPPTAG",
                note.noteId
            )
        }
    }

    fun onNoteSwipe(note: Note) {
        if (homeScreenNotesState.value.trashEnabled) {
            sendToTrash(note)
        } else {
            deleteNote(note)
        }
    }

    fun clearSearchBar() {
        _homeScreenNotesState.value = _homeScreenNotesState.value.copy(searchBarInput = "")
    }

    private fun getPrefs() {
        viewModelScope.launch {
            dataStore.data.collect { preferences ->

                if (preferences.contains(hideCategoriesKey)) {
                    _homeScreenNotesState.value = _homeScreenNotesState.value.copy(
                        showCategoryTitles = preferences[hideCategoriesKey] ?: true
                    )
                }

                if (preferences.contains(isTrashEnabledKey)) {
                    _homeScreenNotesState.value = _homeScreenNotesState.value.copy(
                        trashEnabled = preferences[isTrashEnabledKey] ?: true
                    )
                }
            }
        }
    }

    init {
        getPrefs()
        getNotesList()
        showCategories()
    }
}
