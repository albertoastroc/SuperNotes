package com.gmail.pentominto.us.supernotes.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.gmail.pentominto.us.supernotes.database.NoteDatabase
import com.gmail.pentominto.us.supernotes.utility.Constants.DATABASE_NAME
import com.gmail.pentominto.us.supernotes.utility.Constants.PREFERENCES_STORE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideDatabaseDao(
        @ApplicationContext app : Context
    ) = Room.databaseBuilder(
        app,
        NoteDatabase::class.java,
        DATABASE_NAME)

//    .createFromAsset("notes_database.db")
        .build()
        .getNoteDatabaseDao()

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext appContext : Context
    ) : DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(PREFERENCES_STORE_NAME) }
        )
    }
}