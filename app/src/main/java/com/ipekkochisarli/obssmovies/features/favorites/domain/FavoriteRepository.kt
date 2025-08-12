package com.ipekkochisarli.obssmovies.features.favorites.domain

import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.FavoriteListType
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.FavoriteMovieUiModel

interface FavoriteRepository {
    suspend fun addFavorite(movie: FavoriteMovieUiModel)

    suspend fun removeFavorite(movieId: Int)

    suspend fun getFavoritesByListType(listType: FavoriteListType): ApiResult<List<FavoriteMovieUiModel>>

    suspend fun isFavorite(movieId: Int): Boolean
}
