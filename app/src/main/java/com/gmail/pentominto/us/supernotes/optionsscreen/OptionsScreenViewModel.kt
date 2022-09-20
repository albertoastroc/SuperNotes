package com.gmail.pentominto.us.supernotes.optionsscreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OptionsScreenViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _categoriesOptionState : MutableState<Boolean> = mutableStateOf(false)
    val categoriesOptionsState : State<Boolean> = _categoriesOptionState

    fun categoriesPrefToggle() {

        viewModelScope.launch {

            dataStore.edit { settings ->

                _categoriesOptionState.value = !_categoriesOptionState.value
                val hideCategoriesKey = booleanPreferencesKey("hide_categories")
                settings[hideCategoriesKey] = categoriesOptionsState.value

                Log.d(
                    "TAG",
                    "categoriesPrefToggle: ${categoriesOptionsState.value}"
                )
            }
        }
    }

    fun getPrefs() {

        viewModelScope.launch {

            val hideCategoryKey = booleanPreferencesKey("hide_categories")

            dataStore.data.collect() { preferences ->

                if (preferences.contains(hideCategoryKey)) {

                    _categoriesOptionState.value = preferences[hideCategoryKey] !!
                }
            }
        }
    }

    init {
        getPrefs()
    }
}