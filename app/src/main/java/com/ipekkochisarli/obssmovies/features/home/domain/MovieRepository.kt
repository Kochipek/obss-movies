package com.ipekkochisarli.obssmovies.features.home.domain

import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.home.HomeSectionType

interface MovieRepository {
    suspend fun getMovieListBySection(section: HomeSectionType): ApiResult<List<MovieUiModel>>

    suspend fun searchMovie(query: String): ApiResult<List<MovieUiModel>>
}
