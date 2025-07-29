package com.ipekkochisarli.obssmovies.features.movie.domain

import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.movie.HomeSectionType

interface MovieRepository{
    suspend fun getMovieListBySection(section: HomeSectionType): ApiResult<List<MovieUiModel>>
}