package com.ipekkochisarli.obssmovies.features.favorites.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_movies",
    primaryKeys = ["id", "listType"],
)
data class FavoriteMovieEntity(
    val id: Int,
    val userId: String,
    val title: String,
    val posterUrl: String,
    val releaseYear: String,
    val listType: String,
)
