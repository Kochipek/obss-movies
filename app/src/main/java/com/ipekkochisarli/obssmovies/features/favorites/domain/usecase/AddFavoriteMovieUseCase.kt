package com.ipekkochisarli.obssmovies.features.favorites.domain.usecase

import com.ipekkochisarli.obssmovies.features.favorites.domain.FavoriteRepository
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.FavoriteMovieUiModel

class AddFavoriteMovieUseCase(
    private val repository: FavoriteRepository,
) {
    suspend operator fun invoke(movie: FavoriteMovieUiModel) = repository.addFavorite(movie)
}
