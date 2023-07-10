package com.moviemagic.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.moviemagic.ui.HomeScreen
import com.moviemagic.ui.MovieDetails


@Composable
fun MovieApp() {

    val navController = rememberNavController()
    MovieNavHost(
        navController = navController
    )
}

@Composable
fun MovieNavHost(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                modifier = Modifier,
                onMovieClick = {
                    navController.navigate("movieDetails/${it}")
                }
            )
        }
        composable(
            "movieDetails/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) {
            MovieDetails()
        }
    }
}