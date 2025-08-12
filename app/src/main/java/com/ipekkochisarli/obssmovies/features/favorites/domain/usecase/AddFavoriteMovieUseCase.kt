package com.ipekkochisarli.obssmovies.features.favorites.domain.usecase

import com.ipekkochisarli.obssmovies.features.favorites.domain.FavoriteRepository
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.FavoriteListType
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.FavoriteMovieUiModel
import javax.inject.Inject

class AddFavoriteMovieUseCase
    @Inject
    constructor(
        private val repository: FavoriteRepository,
    ) {
        suspend operator fun invoke(
            movie: FavoriteMovieUiModel,
            listType: FavoriteListType,
        ) = repository.addFavorite(movie)
    }
