package com.gmail.pentominto.us.supernotes

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStore : DataStore<Preferences>
) : ViewModel() {

    val userDarkThemeKey = booleanPreferencesKey("user_theme")
    val userIdKey = stringPreferencesKey("user_id")

    val isDarkThemeState = mutableStateOf(false)

    fun setUpFirebaseId() {

        viewModelScope.launch {
            dataStore.edit { preferences ->

                if (! preferences.contains(userIdKey)) {
                    val userId = UUID.randomUUID().toString()
                    preferences[userIdKey] = userId
                }
            }

            dataStore.data.collect { preferences ->

                Firebase.crashlytics.setUserId(preferences[userIdKey] ?: String())
                Firebase.analytics.setUserId(preferences[userIdKey] ?: String())
            }
        }
    }

    fun isDarkThemePreferred() {

        viewModelScope.launch {

            dataStore.data.collect { preferences ->

                if (preferences.contains(userDarkThemeKey)) {

                    isDarkThemeState.value = preferences[userDarkThemeKey] ?: false

                }
            }
        }
    }

    init {

        isDarkThemePreferred()
    }

}