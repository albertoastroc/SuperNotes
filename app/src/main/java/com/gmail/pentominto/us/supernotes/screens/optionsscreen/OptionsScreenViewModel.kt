package com.gmail.pentominto.us.supernotes.screens.optionsscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.database.DatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OptionsScreenViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val databaseDao : DatabaseDao
) : ViewModel() {

    private val _optionsScreenState : MutableState<OptionsScreenState> = mutableStateOf(OptionsScreenState())
    val optionsScreenState : State<OptionsScreenState> = _optionsScreenState

    private val hideCategoriesKey = booleanPreferencesKey("hide_categories")
    private val userDarkThemeKey = booleanPreferencesKey("user_theme")
    private val trashEnabled = booleanPreferencesKey("trash_enabled")

    fun categoriesPrefToggle() {

        viewModelScope.launch {

            dataStore.edit { settings ->

                _optionsScreenState.value = _optionsScreenState.value.copy(categoriesOption = !_optionsScreenState.value.categoriesOption)

                settings[hideCategoriesKey] = _optionsScreenState.value.categoriesOption
            }
        }
    }

    fun themeToggle() {

        viewModelScope.launch {

            dataStore.edit { settings ->

                _optionsScreenState.value = _optionsScreenState.value.copy(darkThemeOption = !_optionsScreenState.value.darkThemeOption)

                settings[userDarkThemeKey] = _optionsScreenState.value.darkThemeOption
            }
        }
    }

    fun trashFolderToggle() {

        viewModelScope.launch {

            dataStore.edit { settings ->

                _optionsScreenState.value = _optionsScreenState.value.copy(trashEnabled =
                !_optionsScreenState.value.trashEnabled)

                settings[trashEnabled] = _optionsScreenState.value.trashEnabled
            }
        }
    }

    fun getPrefs() {

        viewModelScope.launch {

            dataStore.data.collect() { preferences ->

                if (preferences.contains(hideCategoriesKey)) {

                    _optionsScreenState.value = _optionsScreenState.value.copy(
                        categoriesOption = preferences[hideCategoriesKey] ?: false
                    )
                }

                if (preferences.contains(userDarkThemeKey)) {

                    _optionsScreenState.value = _optionsScreenState.value.copy(
                        darkThemeOption = preferences[userDarkThemeKey] ?: false
                    )
                }

                if (preferences.contains(trashEnabled)) {

                    _optionsScreenState.value = _optionsScreenState.value.copy(
                        trashEnabled = preferences[trashEnabled] ?: true
                    )
                }
            }
        }
    }

    fun deleteAllNotes() {

        viewModelScope.launch {

            databaseDao.deleteAllNotes()
        }
    }

    fun deleteAllTrashNotes() {

        viewModelScope.launch {

            databaseDao.deleteAllTrashNotes()
        }
    }

    init {
        getPrefs()
    }

}