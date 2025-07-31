package com.ipekkochisarli.obssmovies.features.movie.domain

import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.movie.HomeSectionType
import javax.inject.Inject

class GetMovieListBySectionUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        suspend operator fun invoke(section: HomeSectionType): ApiResult<List<MovieUiModel>> =
            movieRepository.getMovieListBySection(section)
    }
