package com.ipekkochisarli.obssmovies.features.movielist

import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel

data class MovieListUiState(
    val results: List<MovieUiModel> = emptyList(),
    val viewType: MovieViewType = MovieViewType.LIST,
    val isLoading: Boolean = false,
)
