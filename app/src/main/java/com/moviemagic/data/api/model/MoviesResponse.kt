package com.moviemagic.data.api.model

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    val page: String,
    val entries: Int,
    val results: List<MovieResult>
)

data class MovieResult(
    val id: String,
    @SerializedName("primaryImage") val imageSource: ImageSource,
    @SerializedName("releaseYear") val releaseDate: ReleaseDate,
    @SerializedName("titleText") val titleData:TitleData
)

data class TitleData(
    @SerializedName("text") val title: String?
)

data class ImageSource(
    val url: String?
)

data class ReleaseDate(
    val year: Int?
)