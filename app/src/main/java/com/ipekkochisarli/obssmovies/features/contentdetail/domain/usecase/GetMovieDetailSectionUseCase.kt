package com.ipekkochisarli.obssmovies.features.contentdetail.domain.usecase

import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.contentdetail.DetailSectionType
import com.ipekkochisarli.obssmovies.features.contentdetail.data.dto.DetailSectionResult
import com.ipekkochisarli.obssmovies.features.home.domain.MovieRepository
import javax.inject.Inject

class GetMovieDetailSectionUseCase
    @Inject
    constructor(
        private val repository: MovieRepository,
    ) {
        suspend operator fun invoke(
            movieId: Int,
            section: DetailSectionType,
        ): ApiResult<DetailSectionResult> = repository.getMovieDetailSection(movieId, section)
    }
