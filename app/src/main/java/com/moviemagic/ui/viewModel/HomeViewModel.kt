package com.moviemagic.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviemagic.domain.entities.MovieEntity
import com.moviemagic.domain.usecase.GetMoviesUseCase
import com.moviemagic.domain.usecase.UseCaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val moviesUseCase: GetMoviesUseCase): ViewModel() {

    private val _viewState = MutableStateFlow(MovieViewState())
    val viewState = _viewState.asStateFlow()

    init {
        loadMovies()
    }

    private fun loadMovies() {
        val pageToLoad = viewState.value.pageToLoad
        if(viewState.value.inProgress || pageToLoad == null) return

        _viewState.update { it.copy(inProgress = true) }

        viewModelScope.launch {

            when (val useCaseResult = moviesUseCase.invoke(pageToLoad)) {
                is UseCaseResult.Success -> {
                    val canLoadMore = useCaseResult.data?.results?.size == 10
                    val nextPage = if (canLoadMore) pageToLoad + 1 else pageToLoad
                    val currentListItems = viewState.value.movies.toMutableList()

                    _viewState.update {
                        it.copy(
                            inProgress = false,
                            movies = currentListItems + useCaseResult.data!!.results.map {
                                MovieEntity(
                                    id = it.id,
                                    title = it.titleData.title ?: "",
                                    imageUrl = it.imageSource?.url ?: "",
                                    releaseDate = it.releaseDate.year ?: -1
                                )
                            },
                            canLoadMore = canLoadMore,
                            page = nextPage
                        )
                    }
                }

                is UseCaseResult.Error -> {
                    _viewState.update {
                        it.copy(
                            inProgress = false,
                            movieDialog = ErrorDialog(useCaseResult.error.errorMessage)
                        )
                    }
                }
            }
        }
    }

    fun onLastListElementReached() {
        loadMovies()
    }
}

data class MovieViewState(
    val inProgress: Boolean = false,
    val movies: List<MovieEntity> = emptyList(),
    val page: Int = 1,
    val canLoadMore: Boolean  = true,
    val movieDialog: MovieDialog? = null
) {
    val pageToLoad = if (canLoadMore) page else null
    val showEmpty = !inProgress && movies.isEmpty()
}

sealed interface MovieDialog

data class ErrorDialog(val errorDialog: String) : MovieDialog