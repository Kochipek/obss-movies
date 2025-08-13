package com.ipekkochisarli.obssmovies.features.home.domain

import androidx.paging.PagingData
import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.contentdetail.DetailSectionType
import com.ipekkochisarli.obssmovies.features.contentdetail.data.dto.DetailSectionResult
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.ContentDetailUiModel
import com.ipekkochisarli.obssmovies.features.home.HomeSectionType
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovieListBySection(section: HomeSectionType): Flow<PagingData<MovieUiModel>>

    suspend fun getMovies(section: HomeSectionType): ApiResult<List<MovieUiModel>>

    fun searchMovie(query: String): Flow<PagingData<MovieUiModel>>

    suspend fun getMovieDetail(movieId: Int): ApiResult<ContentDetailUiModel>

    suspend fun getMovieDetailSection(
        movieId: Int,
        section: DetailSectionType,
    ): ApiResult<DetailSectionResult>
}
