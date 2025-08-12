package com.ipekkochisarli.obssmovies.features.favorites.domain.usecase

import com.ipekkochisarli.obssmovies.features.favorites.domain.FavoriteRepository
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.LibraryCategoryType
import javax.inject.Inject

class RemoveFavoriteMovieUseCase
    @Inject
    constructor(
        private val repository: FavoriteRepository,
    ) {
        suspend operator fun invoke(
            movieId: Int,
            listType: LibraryCategoryType,
        ) = repository.removeFavorite(movieId, listType)
    }
