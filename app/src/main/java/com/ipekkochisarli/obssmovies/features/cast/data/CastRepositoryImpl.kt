package com.ipekkochisarli.obssmovies.features.cast.data

import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.core.network.AppError
import com.ipekkochisarli.obssmovies.features.cast.domain.CastRepository
import com.ipekkochisarli.obssmovies.features.cast.domain.CastUiModel
import com.ipekkochisarli.obssmovies.features.home.data.remote.service.MovieApiService
import javax.inject.Inject

class CastDetailRepositoryImpl
    @Inject
    constructor(
        private val apiService: MovieApiService,
    ) : CastRepository {
        override suspend fun getCastDetail(castId: Int): ApiResult<CastUiModel> =
            try {
                val dto = apiService.getCastDetail(castId)
                val detail = dto.toDomain() ?: throw IllegalStateException("Cast detail not found")
                ApiResult.Success(detail)
            } catch (e: Exception) {
                ApiResult.Error(AppError.fromException(e))
            }
    }
