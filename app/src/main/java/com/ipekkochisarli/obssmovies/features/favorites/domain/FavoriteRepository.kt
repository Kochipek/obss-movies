package com.ipekkochisarli.obssmovies.features.favorites.domain

import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.FavoriteMovieUiModel
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.LibraryCategoryType

interface FavoriteRepository {
    suspend fun addFavorite(movie: FavoriteMovieUiModel)

    suspend fun removeFavorite(
        movieId: Int,
        listType: LibraryCategoryType,
    )

    suspend fun getFavoritesByListType(listType: LibraryCategoryType): ApiResult<List<FavoriteMovieUiModel>>
}
