package com.gmail.pentominto.us.supernotes.screens.optionsscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.repositories.LocalRepository
import com.gmail.pentominto.us.supernotes.utility.Constants.FIREBASE_ID_KEY
import com.gmail.pentominto.us.supernotes.utility.Constants.USER_HIDE_CATEGORIES_KEY
import com.gmail.pentominto.us.supernotes.utility.Constants.USER_TRASH_ENABLED_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OptionsScreenViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val repository: LocalRepository
) : ViewModel() {

    private val _optionsScreenState: MutableState<OptionsScreenState> = mutableStateOf(
        OptionsScreenState()
    )
    val optionsScreenState: State<OptionsScreenState> = _optionsScreenState

    private val hideCategoriesKey = booleanPreferencesKey(USER_HIDE_CATEGORIES_KEY)
    private val trashEnabledKey = booleanPreferencesKey(USER_TRASH_ENABLED_KEY)
    private val userIdKey = stringPreferencesKey(FIREBASE_ID_KEY)

    fun categoriesPrefToggle() {
        viewModelScope.launch {
            dataStore.edit { settings ->

                _optionsScreenState.value = _optionsScreenState.value.copy(
                    categoriesOption = !_optionsScreenState.value.categoriesOption
                )

                settings[hideCategoriesKey] = _optionsScreenState.value.categoriesOption
            }
        }
    }

    fun trashFolderToggle() {
        viewModelScope.launch {
            dataStore.edit { settings ->

                _optionsScreenState.value = _optionsScreenState.value.copy(
                    trashEnabled =
                    !_optionsScreenState.value.trashEnabled
                )

                settings[trashEnabledKey] = _optionsScreenState.value.trashEnabled
            }
        }
    }

    private fun getPreferences() {
        viewModelScope.launch {
            dataStore.data.collect { preferences ->

                if (preferences.contains(hideCategoriesKey)) {
                    _optionsScreenState.value = _optionsScreenState.value.copy(
                        categoriesOption = preferences[hideCategoriesKey] ?: true
                    )
                }

                if (preferences.contains(trashEnabledKey)) {
                    _optionsScreenState.value = _optionsScreenState.value.copy(
                        trashEnabled = preferences[trashEnabledKey] ?: true
                    )
                }

                if (preferences.contains(userIdKey)) {
                    _optionsScreenState.value = _optionsScreenState.value.copy(
                        userId = preferences[userIdKey] ?: String()
                    )
                }
            }
        }
    }

    fun deleteHomeScreenNotes() {
        viewModelScope.launch {
            repository.deleteHomeScreenNotes()
        }
    }

    fun deleteTrashNotes() {
        viewModelScope.launch {
            repository.deleteTrashNotes()
        }
    }

    init {
        getPreferences()
    }
}
