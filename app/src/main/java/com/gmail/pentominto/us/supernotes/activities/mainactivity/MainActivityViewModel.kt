package com.gmail.pentominto.us.supernotes.activities.mainactivity

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.pentominto.us.supernotes.data.Category
import com.gmail.pentominto.us.supernotes.repositories.LocalRepository
import com.gmail.pentominto.us.supernotes.utility.Constants.FIREBASE_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStore : DataStore<Preferences>,
    private val repository : LocalRepository
) : ViewModel() {

    val userIdKey = stringPreferencesKey(FIREBASE_ID_KEY)

    private fun setUpFirebaseId() {
        viewModelScope.launch {
            dataStore.edit { preferences ->

                if (! preferences.contains(userIdKey)) {
                    val userId = UUID.randomUUID().toString()
                    preferences[userIdKey] = userId
                }
            }

            dataStore.data.collect { preferences ->

//                Firebase.crashlytics.setUserId(preferences[userIdKey] ?: String())
//                Firebase.analytics.setUserId(preferences[userIdKey] ?: String())
            }
        }
    }

    private fun setDefaultCategory() {
        viewModelScope.launch {
            if (! repository.defaultCategoryExists()) {
                repository.insertCategory(
                    Category()
                )
            }
        }
    }

    init {

        setDefaultCategory()
        setUpFirebaseId()
    }
}
