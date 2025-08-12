package com.ipekkochisarli.obssmovies.features.favorites.data

import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.core.network.AppError
import com.ipekkochisarli.obssmovies.features.favorites.data.local.dao.FavoriteMovieDao
import com.ipekkochisarli.obssmovies.features.favorites.domain.FavoriteRepository
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.FavoriteMovieUiModel
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.LibraryCategoryType
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.toDomain
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.toEntity
import com.ipekkochisarli.obssmovies.features.login.domain.AuthRepository
import javax.inject.Inject

class FavoriteRepositoryImpl
    @Inject
    constructor(
        private val dao: FavoriteMovieDao,
        private val authRepository: AuthRepository,
    ) : FavoriteRepository {
        override suspend fun addFavorite(movie: FavoriteMovieUiModel) {
            val userId = authRepository.getCurrentUserId() ?: return
            dao.insertFavorite(movie.toEntity(userId))
        }

        override suspend fun removeFavorite(movieId: Int) {
            val userId = authRepository.getCurrentUserId() ?: return
            dao.deleteFavorite(movieId, userId)
        }

        override suspend fun getFavoritesByListType(listType: LibraryCategoryType): ApiResult<List<FavoriteMovieUiModel>> =
            try {
                val userId =
                    authRepository.getCurrentUserId()
                        ?: return ApiResult.Error(AppError.AuthError.UserNotFound)

                val entities = dao.getFavoritesByListType(listType.name, userId)
                ApiResult.Success(entities.map { it.toDomain() })
            } catch (e: Exception) {
                ApiResult.Error(AppError.fromException(e))
            }

        override suspend fun isFavorite(movieId: Int): Boolean {
            val userId = authRepository.getCurrentUserId() ?: return false
            return dao.isFavorite(movieId, userId)
        }
    }
