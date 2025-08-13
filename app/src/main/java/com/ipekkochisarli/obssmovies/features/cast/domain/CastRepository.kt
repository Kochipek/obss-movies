package com.ipekkochisarli.obssmovies.features.cast.domain

import com.ipekkochisarli.obssmovies.core.network.ApiResult

interface CastRepository {
    suspend fun getCastDetail(castId: Int): ApiResult<CastUiModel>
}
