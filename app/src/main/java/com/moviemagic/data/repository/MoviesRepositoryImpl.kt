package com.moviemagic.data.repository

import com.moviemagic.AppService
import com.moviemagic.data.api.db.MovieDao
import com.moviemagic.data.api.model.MoviesResponse
import com.moviemagic.domain.entities.MovieEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val appService: AppService,
    private val movieDao: MovieDao
) : MoviesRepository {

    override suspend fun fetchMovies(page: Int): MoviesResponse? {
        val moviesResponse = appService.getMovies(page)
        if (moviesResponse != null) {
            insertMoviesToDB(moviesResponse)
        }
        return moviesResponse
    }

    suspend fun insertMoviesToDB(moviesResponse: MoviesResponse) {
        mapMovieResponseToMovieEntities(moviesResponse)?.let { movieDao.insertAll(it) }
    }

    private fun mapMovieResponseToMovieEntities(moviesResponse: MoviesResponse): List<MovieEntity>? {
        return moviesResponse?.results?.map {
            MovieEntity(
                id = it.id,
                title = it.titleData.title ?: "",
                imageUrl = it.imageSource?.url ?: "",
                releaseDate = it.releaseDate.year ?: -1
            )
        }
    }

    override suspend fun getMovies(limit: Int, page: Int): Flow<List<MovieEntity>> = movieDao.getMovies(limit = limit, page = page)

    override suspend fun getMovieById(id: String): Flow<MovieEntity> = movieDao.getMovieById(id)

}