package com.ipekkochisarli.obssmovies.features.search.domain

import androidx.paging.PagingData
import com.ipekkochisarli.obssmovies.features.home.domain.MovieRepository
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        operator fun invoke(query: String): Flow<PagingData<MovieUiModel>> = movieRepository.searchMovie(query)
    }
