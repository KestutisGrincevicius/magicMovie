package com.moviemagic.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    val title: String,
    val imageUrl: String,
    val releaseDate: Int
)