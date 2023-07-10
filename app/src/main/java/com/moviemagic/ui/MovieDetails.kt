package com.moviemagic.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.moviemagic.R
import com.moviemagic.ui.theme.Styles
import com.moviemagic.ui.viewModel.MovieDetailsViewModel

@Composable
fun MovieDetails(
    movieDetailsViewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val viewState by movieDetailsViewModel.viewState.collectAsStateWithLifecycle()
    val movieEntity = viewState.movieEntity
    Surface {
        if(movieEntity != null) {
            MovieDetailUi(
                title = movieEntity.title,
                releaseYear = movieEntity.releaseDate,
                imageUrl = movieEntity.imageUrl,
            )
        }
    }
}

@Composable
fun MovieDetailUi(
    modifier: Modifier = Modifier,
    title: String,
    releaseYear: Int,
    imageUrl: String,
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(15.dp)
    )
    {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(imageUrl).crossfade(true).build(),
            placeholder = painterResource(id = R.drawable.movie_placeholder),
            contentScale = ContentScale.FillWidth,
            error = painterResource(id = R.drawable.movie_placeholder),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .height(180.dp)
                .padding(end = 10.dp)
        )
        Text(
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(15.dp),
            style = Styles.title,
            text = title
        )
        Text(
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(15.dp),
            style = Styles.content,
            text = "Release year: $releaseYear"
        )
    }
}