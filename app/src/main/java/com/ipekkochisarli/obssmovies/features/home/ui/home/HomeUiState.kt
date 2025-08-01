package com.ipekkochisarli.obssmovies.features.home.ui

import com.ipekkochisarli.obssmovies.features.home.HomeSectionType
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel

data class HomeUiState(
    val type: HomeSectionType,
    val title: String,
    val movies: List<MovieUiModel>,
    val carouselImages: List<String> = emptyList(),
)
