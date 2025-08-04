package com.ipekkochisarli.obssmovies.features.search.presentation

import com.ipekkochisarli.obssmovies.common.MovieViewType
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel

data class SearchUiState(
    val results: List<MovieUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val viewType: MovieViewType = MovieViewType.LIST,
    val query: String = "",
)
