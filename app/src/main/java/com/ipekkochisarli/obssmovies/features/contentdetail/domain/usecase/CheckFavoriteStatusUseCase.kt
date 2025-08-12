package com.ipekkochisarli.obssmovies.features.contentdetail.domain.usecase

import com.ipekkochisarli.obssmovies.features.favorites.domain.FavoriteRepository
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.LibraryCategoryType

class CheckFavoriteStatusUseCase(
    private val favoritesRepository: FavoriteRepository,
) {
    suspend operator fun invoke(
        movieId: Int,
        listType: LibraryCategoryType,
    ): Boolean = favoritesRepository.isFavorite(movieId, listType)
}
