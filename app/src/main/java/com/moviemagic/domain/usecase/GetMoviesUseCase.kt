package com.moviemagic.domain.usecase

import com.moviemagic.data.api.model.MoviesResponse
import com.moviemagic.data.repository.MoviesRepository
import com.moviemagic.domain.entities.MovieEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
): BaseUseCase<Int, MoviesResponse?>() {

    override suspend fun execute(page: Int): MoviesResponse? {
        return moviesRepository.fetchMovies(page)
    }

    suspend fun getMovies(limit: Int, page: Int): Flow<List<MovieEntity>> {
        return moviesRepository.getMovies(limit = limit, page = page)
    }
}