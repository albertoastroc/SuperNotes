package com.gmail.pentominto.us.supernotes.di

import android.content.Context
import androidx.room.Room
import com.gmail.pentominto.us.supernotes.Utility.Constants.DATABASE_NAME
import com.gmail.pentominto.us.supernotes.database.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabaseDao(
        @ApplicationContext app : Context
    ) = Room.databaseBuilder(
        app ,
        NoteDatabase::class.java ,
        DATABASE_NAME
    ).build().getNoteDatabaseDao()

}