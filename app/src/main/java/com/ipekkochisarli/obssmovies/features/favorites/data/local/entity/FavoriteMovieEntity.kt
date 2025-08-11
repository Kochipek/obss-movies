package com.ipekkochisarli.obssmovies.features.favorites.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterUrl: String,
    val releaseYear: String,
    val listType: String,
)
