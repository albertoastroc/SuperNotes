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

    private val _categoriesOptionState : MutableState<Boolean> = mutableStateOf(false)
    val categoriesOptionsState : State<Boolean> = _categoriesOptionState

    private val _useDarkThemeState : MutableState<Boolean> = mutableStateOf(false)
    val useDarkThemeState : State<Boolean> = _useDarkThemeState

    val hideCategoriesKey = booleanPreferencesKey("hide_categories")
    val userDarkThemeKey = booleanPreferencesKey("user_theme")

    init {
        getPrefs()
    }

    fun categoriesPrefToggle() {

        viewModelScope.launch {

            dataStore.edit { settings ->

                _categoriesOptionState.value = !_categoriesOptionState.value
                settings[hideCategoriesKey] = categoriesOptionsState.value
            }
        }
    }

    fun themeToggle() {

        viewModelScope.launch {

            dataStore.edit { settings ->

                _useDarkThemeState.value = !_useDarkThemeState.value
                settings[userDarkThemeKey] = useDarkThemeState.value
            }
        }
    }

    fun getPrefs() {

        viewModelScope.launch {

            dataStore.data.collect() { preferences ->

                if (preferences.contains(hideCategoriesKey)) {

                    _categoriesOptionState.value = preferences[hideCategoriesKey] ?: false
                }

                if (preferences.contains(userDarkThemeKey)) {

                    _useDarkThemeState.value = preferences[userDarkThemeKey] ?: false
                }
            }
        }
    }

    fun deleteAllNotes() {

        viewModelScope.launch {

            databaseDao.deleteAllNotes()
        }
    }

}