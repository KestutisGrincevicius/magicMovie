@file:OptIn(ExperimentalMaterial3Api::class)

package com.moviemagic.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.moviemagic.R
import com.moviemagic.domain.entities.MovieEntity
import com.moviemagic.ui.base.rememberScrollContext
import com.moviemagic.ui.viewModel.HomeViewModel
import com.moviemagic.ui.viewModel.MovieViewState
import com.moviemagic.ui.theme.Styles

@Composable
fun HomeScreen(
    modifier: Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    onMovieClick: (String) -> Unit = {}
) {
    val viewState by homeViewModel.viewState.collectAsStateWithLifecycle()
    MoviesUI(
        viewState = viewState,
        onMovieSelected = onMovieClick,
        onLastItemReached = homeViewModel::onLastListElementReached
    )
}

@Composable
fun MoviesUI(
    viewState: MovieViewState,
    onMovieSelected: (id: String) -> Unit,
    onLastItemReached: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold { paddingValues ->
            val listState = rememberLazyListState()
            val scrollContext = rememberScrollContext(listState = listState)
            LaunchedEffect(key1 = scrollContext.isBottom) {
                if (scrollContext.isBottom && !viewState.inProgress) {
                    onLastItemReached()
                }
            }
            if (viewState.movies.isNotEmpty()) {
                MovieList(
                    paddingValues = paddingValues,
                    listState = listState,
                    movies = viewState.movies,
                    onMovieClick = onMovieSelected
                )
            }
            AnimatedVisibility(visible = viewState.inProgress) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = Color.Blue
                )
            }
        }
    }
}

@Composable
fun MovieList(
    paddingValues: PaddingValues,
    listState: LazyListState,
    movies: List<MovieEntity>,
    onMovieClick: (id: String) -> Unit
) {
    LazyColumn(contentPadding = paddingValues, state = listState) {
        movies.forEach { movie ->
            val movieId = movie.id
            item(key = movieId) {
                MovieItem(
                    id = movie.id,
                    title = movie.title,
                    imageUrl = movie.imageUrl,
                    releaseYear = movie.releaseDate,
                    onMovieClick = { onMovieClick(movieId) }
                )
            }
        }
    }
}

@Composable
fun MovieItem(
    modifier: Modifier = Modifier,
    id: String,
    title: String,
    imageUrl: String,
    releaseYear: Int,
    onMovieClick: (id: String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {

        Row(
            modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { onMovieClick(id) }
            )
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(imageUrl).crossfade(true).build(),
                placeholder = painterResource(id = R.drawable.movie_placeholder),
                contentScale = ContentScale.FillWidth,
                error = painterResource(id = R.drawable.movie_placeholder),
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .width(120.dp)
                    .height(180.dp)
                    .padding(end = 10.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    style = Styles.title,
                    text = title
                )
                Text(
                    style = Styles.content,
                    text = "Release year: $releaseYear"
                )
            }
        }
    }
}