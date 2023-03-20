package com.gmail.pentominto.us.supernotes.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.gmail.pentominto.us.supernotes.database.DatabaseDAO
import com.gmail.pentominto.us.supernotes.database.NoteDatabase
import com.gmail.pentominto.us.supernotes.utility.Constants.DATABASE_NAME
import com.gmail.pentominto.us.supernotes.utility.Constants.PREFERENCES_STORE_NAME
import com.gmail.pentominto.us.supernotes.utility.Constants.PREPOP_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        NoteDatabase::class.java,
        DATABASE_NAME
    )
        .createFromAsset(PREPOP_DATABASE_NAME)
        .build()

    @Provides
    fun providesDAO(
        database: NoteDatabase
    ): DatabaseDAO {
        return database.notesDAO()
    }

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(PREFERENCES_STORE_NAME) }
        )
    }
}
