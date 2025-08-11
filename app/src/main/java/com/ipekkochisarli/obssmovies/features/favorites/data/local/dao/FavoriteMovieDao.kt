package com.ipekkochisarli.obssmovies.features.favorites.data.local.dao

import androidx.room.*
import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.favorites.data.local.entity.FavoriteMovieEntity
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.FavoriteMovieUiModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movie: FavoriteMovieEntity)

    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    suspend fun deleteFavorite(movieId: Int)

    @Query("SELECT * FROM favorite_movies WHERE listType = :listType")
    suspend fun getFavoritesByListType(listType: String): List<FavoriteMovieEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId)")
    suspend fun isFavorite(movieId: Int): Boolean
}
