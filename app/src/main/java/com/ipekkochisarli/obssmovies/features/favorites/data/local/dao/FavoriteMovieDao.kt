package com.ipekkochisarli.obssmovies.features.favorites.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ipekkochisarli.obssmovies.features.favorites.data.local.entity.FavoriteMovieEntity

@Dao
interface FavoriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movie: FavoriteMovieEntity)

    @Query("DELETE FROM favorite_movies WHERE id = :movieId AND listType = :listType AND userId = :userId")
    suspend fun deleteFavorite(
        movieId: Int,
        userId: String,
        listType: String,
    )

    @Query("SELECT * FROM favorite_movies WHERE listType = :listType AND userId = :userId")
    suspend fun getFavoritesByListType(
        listType: String,
        userId: String,
    ): List<FavoriteMovieEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId AND userId = :userId)")
    suspend fun isFavorite(
        movieId: Int,
        userId: String,
    ): Boolean
}
