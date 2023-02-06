package com.gmail.pentominto.us.supernotes.di

import com.gmail.pentominto.us.supernotes.repositories.LocalRepository
import com.gmail.pentominto.us.supernotes.repositories.LocalRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)

interface RepositoryModule {

    @Binds
    fun provideLocalRepository(repository : LocalRepositoryImpl) : LocalRepository
}