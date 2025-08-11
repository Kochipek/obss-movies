package com.ipekkochisarli.obssmovies.features.favorites.data

import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.core.network.AppError
import com.ipekkochisarli.obssmovies.features.favorites.data.local.dao.FavoriteMovieDao
import com.ipekkochisarli.obssmovies.features.favorites.domain.FavoriteRepository
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.FavoriteListType
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.FavoriteMovieUiModel
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.toDomain
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.toEntity
import javax.inject.Inject

class FavoriteRepositoryImpl
    @Inject
    constructor(
        private val dao: FavoriteMovieDao,
    ) : FavoriteRepository {
        override suspend fun addFavorite(movie: FavoriteMovieUiModel) {
            dao.insertFavorite(movie.toEntity())
        }

        override suspend fun removeFavorite(movieId: Int) {
            dao.deleteFavorite(movieId)
        }

        override suspend fun getFavoritesByListType(listType: FavoriteListType): ApiResult<List<FavoriteMovieUiModel>> =
            try {
                val entities = dao.getFavoritesByListType(listType.name)
                val movies = entities.map { it.toDomain() }
                ApiResult.Success(movies)
            } catch (e: Exception) {
                ApiResult.Error(AppError.fromException(e))
            }

        override suspend fun isFavorite(movieId: Int): Boolean = dao.isFavorite(movieId)
    }
