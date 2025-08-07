package com.ipekkochisarli.obssmovies.features.contentdetail.domain.usecase

import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.contentdetail.domain.ContentDetailUiModel
import com.ipekkochisarli.obssmovies.features.home.domain.MovieRepository
import javax.inject.Inject

class GetMovieDetailUseCase
    @Inject
    constructor(
        private val repository: MovieRepository,
    ) {
        suspend operator fun invoke(movieId: Int): ApiResult<ContentDetailUiModel> = repository.getMovieDetail(movieId)
    }
