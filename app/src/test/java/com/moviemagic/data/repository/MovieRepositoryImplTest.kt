package com.moviemagic.data.repository
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.moviemagic.AppService
import com.moviemagic.data.api.db.MovieDao
import com.moviemagic.data.api.model.ImageSource
import com.moviemagic.data.api.model.MovieResult
import com.moviemagic.data.api.model.MoviesResponse
import com.moviemagic.data.api.model.ReleaseDate
import com.moviemagic.data.api.model.TitleData
import com.moviemagic.domain.entities.MovieEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MovieRepositoryImplTest {

    private val appService: AppService = mockk(relaxed = true)
    private val movieDao: MovieDao = mockk(relaxed = true)

    private val systemUnderTest = MoviesRepositoryImpl(
        appService = appService,
        movieDao = movieDao
    )

    private val movieResponse = MoviesResponse(
        page = "1",
        entries = 10,
        results = mutableListOf(
            MovieResult(
                id = "tt0000081",
                imageSource = ImageSource(url = "https://test.png"),
                releaseDate = ReleaseDate(year = 1920),
                titleData =  TitleData("TestTitle")
            ),
            MovieResult(
                id = "tt0000082",
                imageSource = ImageSource(url = "https://test1.png"),
                releaseDate = ReleaseDate(year = 1922),
                titleData =  TitleData("TestTitle1")
            ),
            MovieResult(
                id = "tt0000083",
                imageSource = ImageSource(url = "https://test2.png"),
                releaseDate = ReleaseDate(year = 1920),
                titleData =  TitleData("TestTitle2")
            )
        )
    )

    @Test
    fun fetchMovies() = runTest {
        coEvery { appService.getMovies(any()) } returns movieResponse
        systemUnderTest.fetchMovies(0)
        coVerify { appService.getMovies(0) }
        coVerify { systemUnderTest.insertMoviesToDB(moviesResponse = movieResponse) }
    }

    @Test
    fun getMovieById() = runTest {
        val expected = MovieEntity(id = "tt0000081", title = "TestTitle", imageUrl = "https://test.png", releaseDate = 1920)
        coEvery { movieDao.getMovieById("tt0000081") } returns flowOf(expected)
        systemUnderTest.getMovieById("tt0000081").test {
            assertThat(awaitItem()).isEqualTo(expected)
            awaitComplete()
        }
    }

    @Test
    fun getAllMovies() = runTest {
        val expected = mutableListOf<MovieEntity>(
            MovieEntity(id = "tt0000081", title = "titleTest", imageUrl = "imageUrlTest", releaseDate = 1999),
            MovieEntity(id = "tt0000082", title = "titleTest", imageUrl = "imageUrlTest", releaseDate = 1999),
        )
        coEvery { movieDao.getMovies(limit = 10, page = 0) } returns flowOf(expected)

        systemUnderTest.getMovies(10, page = 0).test {
            val item = awaitItem()
            assertThat(item.size).isEqualTo(item.size)
            assertThat(item[0].id).isEqualTo("tt0000081")
            assertThat(item[1].id).isEqualTo("tt0000082")
            awaitComplete()
        }
    }
}