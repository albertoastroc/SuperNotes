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
import com.gmail.pentominto.us.supernotes.data.SavedNote
import com.gmail.pentominto.us.supernotes.database.DatabaseDao
import com.gmail.pentominto.us.supernotes.repositories.LocalRepository
import com.gmail.pentominto.us.supernotes.utility.DateGetter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OptionsScreenViewModel @Inject constructor(
    private val dataStore : DataStore<Preferences>,
    private val repository : LocalRepository
) : ViewModel() {

    private val _optionsScreenState : MutableState<OptionsScreenState> = mutableStateOf(OptionsScreenState())
    val optionsScreenState : State<OptionsScreenState> = _optionsScreenState

    private val hideCategoriesKey = booleanPreferencesKey("hide_categories")
    private val userDarkThemeKey = booleanPreferencesKey("user_theme")
    private val trashEnabledKey = booleanPreferencesKey("trash_enabled")
    private val userIdKey = stringPreferencesKey("user_id")

    fun categoriesPrefToggle() {

        viewModelScope.launch {

            dataStore.edit { settings ->

                _optionsScreenState.value = _optionsScreenState.value.copy(categoriesOption = ! _optionsScreenState.value.categoriesOption)

                settings[hideCategoriesKey] = _optionsScreenState.value.categoriesOption
            }
        }
    }

    fun themeToggle() {

        viewModelScope.launch {

            dataStore.edit { settings ->

                _optionsScreenState.value = _optionsScreenState.value.copy(darkThemeOption = ! _optionsScreenState.value.darkThemeOption)

                settings[userDarkThemeKey] = _optionsScreenState.value.darkThemeOption
            }
        }
    }

    fun trashFolderToggle() {

        viewModelScope.launch {

            dataStore.edit { settings ->

                _optionsScreenState.value = _optionsScreenState.value.copy(
                    trashEnabled =
                    ! _optionsScreenState.value.trashEnabled
                )

                settings[trashEnabledKey] = _optionsScreenState.value.trashEnabled
            }
        }
    }

    private fun getCurrentDate() {

        _optionsScreenState.value = _optionsScreenState.value.copy(
            currentDate = DateGetter.getCurrentDate()
        )
    }

    fun restoreWelcomeNote() {

        viewModelScope.launch {

            repository.insertNote(
                SavedNote(
                    noteTitle = "Welcome",
                    createdDate = DateGetter.getCurrentDate(),
                    noteBody = "Thanks for installing the app.  This note includes some basic info and works as a mini FAQ.\n" +
                            "\n" +
                            "1.  If you have a suggestion for a feature you would like to see you can mention it in your review or send an email to simplenotesacf@gmail.com.\n" +
                            "\n" +
                            "2. There is no save button, the app auto-saves everything.\n" +
                            "\n" +
                            "3. To delete a note, swipe it to the right, to restore a note from the Trash, swipe it to the left.\n" +
                            "\n" +
                            "4. To add and set a category use the menu at the top right of this screen. \n" +
                            "\n" +
                            "5. If this note is deleted, it can later be restored in Options.\n" +
                            "\n",
                    lastModified = DateGetter.getCurrentDate(),
                )
            )
        }
    }

    fun getPrefs() {

        viewModelScope.launch {

            dataStore.data.collect { preferences ->

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

    fun deleteAllNotes() {

        viewModelScope.launch {

            repository.deleteAllNotes()
        }
    }

    fun deleteAllTrashNotes() {

        viewModelScope.launch {

            repository.deleteAllTrashNotes()
        }
    }

    init {
        getPrefs()
        getCurrentDate()
    }
}