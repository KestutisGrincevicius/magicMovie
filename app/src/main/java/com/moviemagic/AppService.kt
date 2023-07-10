package com.moviemagic

import com.moviemagic.data.api.model.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AppService {

    @GET("titles")
    suspend fun getMovies(
        @Query("page") page: Int
    ): MoviesResponse?
}