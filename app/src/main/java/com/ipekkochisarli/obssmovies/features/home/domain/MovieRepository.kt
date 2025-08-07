package com.ipekkochisarli.obssmovies.features.home.domain

import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.contentdetail.DetailSectionType
import com.ipekkochisarli.obssmovies.features.contentdetail.data.dto.DetailSectionResult
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.ContentDetailUiModel
import com.ipekkochisarli.obssmovies.features.home.HomeSectionType

interface MovieRepository {
    suspend fun getMovieListBySection(section: HomeSectionType): ApiResult<List<MovieUiModel>>

    suspend fun searchMovie(query: String): ApiResult<List<MovieUiModel>>

    suspend fun getMovieDetail(movieId: Int): ApiResult<ContentDetailUiModel>

    suspend fun getMovieDetailSection(
        movieId: Int,
        section: DetailSectionType,
    ): ApiResult<DetailSectionResult>
}
