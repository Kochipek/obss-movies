package com.ipekkochisarli.obssmovies.features.favorites.domain.usecase

import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.favorites.domain.FavoriteRepository
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.FavoriteListType
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.FavoriteMovieUiModel

class GetFavoriteMoviesUseCase(
    private val repository: FavoriteRepository,
) {
    suspend operator fun invoke(listType: FavoriteListType): ApiResult<List<FavoriteMovieUiModel>> =
        repository.getFavoritesByListType(listType)
}
