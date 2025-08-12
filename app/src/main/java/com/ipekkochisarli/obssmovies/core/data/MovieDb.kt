package com.ipekkochisarli.obssmovies.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ipekkochisarli.obssmovies.features.favorites.data.local.dao.FavoriteMovieDao
import com.ipekkochisarli.obssmovies.features.favorites.data.local.entity.FavoriteMovieEntity

@Database(
    entities = [FavoriteMovieEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class MovieDb : RoomDatabase() {
    abstract fun favoriteMovieDao(): FavoriteMovieDao
}
