package com.ipekkochisarli.obssmovies.features.search.domain

import com.ipekkochisarli.obssmovies.features.home.domain.MovieRepository
import javax.inject.Inject

class GetSearchUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        suspend operator fun invoke(query: String) = movieRepository.searchMovie(query)
    }
