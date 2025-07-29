package com.ipekkochisarli.obssmovies.features.movie.ui

import com.ipekkochisarli.obssmovies.features.movie.HomeSectionType
import com.ipekkochisarli.obssmovies.features.movie.domain.MovieUiModel

data class HomeUiState(
    val type: HomeSectionType,
    val title: String,
    val movies: List<MovieUiModel>
)