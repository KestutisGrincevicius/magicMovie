package com.moviemagic.di

import com.moviemagic.data.repository.MoviesRepository
import com.moviemagic.data.repository.MoviesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindMoviesRepository(repository: MoviesRepositoryImpl): MoviesRepository
}