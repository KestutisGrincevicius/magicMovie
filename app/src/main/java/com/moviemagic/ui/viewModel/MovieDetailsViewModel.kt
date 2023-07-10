package com.moviemagic.ui.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviemagic.data.repository.MoviesRepository
import com.moviemagic.domain.entities.MovieEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MoviesRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val movieId: String = savedStateHandle.get<String>(MOVIE_ID_SAVED_STATE_KEY)!!

    private val _viewState = MutableStateFlow(MovieDetailsState())
    val viewState = _viewState.asStateFlow()

    init {
        loadMovieDetails(movieId)
    }

    private fun loadMovieDetails(movieId: String) {
        viewModelScope.launch {
            repository.getMovieById(movieId).collect { movie ->
                _viewState.update {
                    it.copy(
                        movieEntity = movie
                    )
                }
            }
        }
    }

    companion object {
        private const val MOVIE_ID_SAVED_STATE_KEY = "id"
    }
}

data class MovieDetailsState(
    val movieEntity: MovieEntity? = null,
    val movieDetailsDialog: MovieDetailsDialog? = null
)

sealed interface MovieDetailsDialog

data class MovieDetailsErrorDialog(val errorDialog: String) : MovieDetailsDialog