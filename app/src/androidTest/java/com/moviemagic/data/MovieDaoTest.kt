package com.moviemagic.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import com.moviemagic.data.api.db.MovieDao
import com.moviemagic.di.AppDatabase
import com.moviemagic.domain.entities.MovieEntity
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var movieDao: MovieDao
    private val movieEntity = listOf(
        MovieEntity(
            id = "tt0000081",
            title = "title1",
            imageUrl = "https//testing.png",
            releaseDate = 1980
        ),
        MovieEntity(
            id = "tt0000082",
            title = "title3",
            imageUrl = "https//testing1.png",
            releaseDate = 1980
        ),
        MovieEntity(
            id = "tt0000083",
            title = "title3",
            imageUrl = "https//testing2.png",
            releaseDate = 1980
        )
    )

    @get:Rule
    var instTaskExecutorRule = InstantTaskExecutorRule()


    @Before fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        movieDao = database.movieDao()

        database.movieDao().insertAll(movieEntity)
    }

    @After fun closeDb() {
        database.close()
    }

    @Test fun databaseContainInsertedValue() = runBlocking {
        val movies = movieDao.getMovies(10, 0)
        assertTrue(movies.first().size == 3)
    }

}