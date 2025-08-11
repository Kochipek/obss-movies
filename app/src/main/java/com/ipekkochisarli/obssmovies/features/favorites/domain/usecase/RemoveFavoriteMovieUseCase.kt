package com.ipekkochisarli.obssmovies.features.favorites.domain.usecase

import com.ipekkochisarli.obssmovies.features.favorites.domain.FavoriteRepository

class RemoveFavoriteMovieUseCase(
    private val repository: FavoriteRepository,
) {
    suspend operator fun invoke(movieId: Int) = repository.removeFavorite(movieId)
}
