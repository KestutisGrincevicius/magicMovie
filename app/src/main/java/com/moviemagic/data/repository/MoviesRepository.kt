package com.moviemagic.data.repository

import com.moviemagic.data.api.model.MoviesResponse
import com.moviemagic.domain.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    suspend fun fetchMovies(page: Int): MoviesResponse?

    suspend fun getMovies(limit: Int, page: Int): Flow<List<MovieEntity>>

    suspend fun getMovieById(id: String): Flow<MovieEntity>
}